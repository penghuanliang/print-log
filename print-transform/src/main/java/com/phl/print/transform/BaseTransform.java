package com.phl.print.transform;

import com.android.build.api.transform.Context;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.ide.common.internal.WaitableExecutor;
import com.google.common.io.Files;
import com.phl.print.transform.asm.BaseWeaver;
import com.phl.print.transform.asm.ClassLoaderHelper;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class BaseTransform extends Transform {

    private Project project;
    private Logger logger;
    private boolean emptyRun = false;
    protected BaseWeaver bytecodeWeaver;
    private WaitableExecutor waitableExecutor;

    public static final Set<QualifiedContent.Scope> SCOPES = new HashSet<>();

    public BaseTransform(Project project) {
        this.project = project;
        this.logger = project.getLogger();
        waitableExecutor = WaitableExecutor.useGlobalSharedThreadPool();
    }

    static {
        SCOPES.add(QualifiedContent.Scope.PROJECT);
        SCOPES.add(QualifiedContent.Scope.SUB_PROJECTS);
        SCOPES.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return SCOPES;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public boolean isCacheable() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        RunVariant runVariant = getRunVariant();
        Context context = transformInvocation.getContext();
        boolean isIncremental = transformInvocation.isIncremental();
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        Collection<TransformInput> referencedInputs = transformInvocation.getReferencedInputs();

        if ("debug".equals(context.getVariantName())) {
            emptyRun = runVariant == RunVariant.RELEASE || runVariant == RunVariant.NEVER;
        } else if ("release".equals(context.getVariantName())) {
            emptyRun = runVariant == RunVariant.DEBUG || runVariant == RunVariant.NEVER;
        }

        logger.warn(getName() + " isIncremental = " + isIncremental + ", runVariant = "
                + runVariant + ", emptyRun = " + emptyRun + ", inDuplcatedClassSafeMode = " + inDuplicatedClassSafeMode());

        long startTime = System.currentTimeMillis();

        if (!isIncremental) {
            outputProvider.deleteAll();
        }

        URLClassLoader classLoader = ClassLoaderHelper.getClassLoader(inputs, referencedInputs, project);
        this.bytecodeWeaver.setClassLoader(classLoader);

        boolean flagForCleanDexBuilderFolder = false;
        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                Status status = jarInput.getStatus();
                File dest = outputProvider.getContentLocation(jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(), Format.JAR);
                if (!emptyRun && isIncremental) {
                    switch (status) {
                        case NOTCHANGED:
                            break;
                        case CHANGED:
                        case ADDED:
                            transformJar(jarInput.getFile(), dest, status);
                            break;
                        case REMOVED:
                            if (dest.exists()) {
                                FileUtils.forceDelete(dest);
                            }
                            break;
                    }
                } else {
                    //ignore
                    if (inDuplicatedClassSafeMode() && !isIncremental && !flagForCleanDexBuilderFolder) {
                        cleanDexBuilderFolder(dest);
                        flagForCleanDexBuilderFolder = true;
                    }
                    transformJar(jarInput.getFile(), dest, status);
                }
            }

            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(),
                        directoryInput.getScopes(),
                        Format.DIRECTORY);
                FileUtils.forceMkdir(dest);
                if (!emptyRun && isIncremental) {
                    String srcDirPath = directoryInput.getFile().getAbsolutePath();
                    String destDirPath = dest.getAbsolutePath();
                    Map<File, Status> fileStatusMap = directoryInput.getChangedFiles();
                    for (Map.Entry<File, Status> changedFile : fileStatusMap.entrySet()) {
                        Status status = changedFile.getValue();
                        File inputFile = changedFile.getKey();
                        String destFilePath = inputFile.getAbsolutePath().replace(srcDirPath, destDirPath);
                        File destFile = new File(destFilePath);
                        switch (status) {
                            case NOTCHANGED:
                                break;
                            case REMOVED:
                                if (destFile.exists()) {
                                    //noinspection ResultOfMethodCallIgnored
                                    destFile.delete();
                                }
                                break;
                            case ADDED:
                            case CHANGED:
                                try {
                                    FileUtils.touch(destFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Files.createParentDirs(destFile);
                                }
                                transformSingleFile(inputFile, destFile, srcDirPath);
                                break;
                        }
                    }
                } else {
                    transformDir(directoryInput.getFile(), dest);
                }
            }
        }

        waitableExecutor.waitForTasksWithQuickFail(true);
        long costTime = System.currentTimeMillis() - startTime;
        logger.warn((getName() + " costed " + costTime + "ms"));
    }

    private void transformDir(File inputDir, File outputDir) throws IOException {
        logger.warn("[transformDir] inputDir:" + inputDir.getAbsolutePath() + "\noutputDir:" + outputDir.getAbsolutePath());
        if (emptyRun) {
            FileUtils.copyDirectory(inputDir, outputDir);
            return;
        }

        final String inputDirPath = inputDir.getAbsolutePath();
        final String outputDirPath = outputDir.getAbsolutePath();

        if (inputDir.isDirectory()) {
            for (File file : com.android.utils.FileUtils.getAllFiles(inputDir)) {
                waitableExecutor.execute(() -> {
                    String filePath = file.getAbsolutePath();
                    File outFile = new File(filePath.replace(inputDirPath, outputDirPath));
                    bytecodeWeaver.weaveSingleClassToFile(file, outFile, inputDirPath);
                    return null;
                });
            }
        }

    }

    private void transformSingleFile(File inputFile, File destFile, String srcDirPath) {
        waitableExecutor.execute(() -> {
            bytecodeWeaver.weaveSingleClassToFile(inputFile, destFile, srcDirPath);
            return null;
        });
    }

    private void cleanDexBuilderFolder(File dest) {
        waitableExecutor.execute(() -> {
            try {
                String dexBuilderDir = replaceLastPart(dest.getAbsolutePath(), getName(), "dexBuilder");
                //intermediates/transforms/dexBuilder/debug
                File file = new File(dexBuilderDir).getParentFile();
                project.getLogger().warn("clean dexBuilder folder = " + file.getAbsolutePath());
                if (file.exists() && file.isDirectory()) {
                    com.android.utils.FileUtils.deleteDirectoryContents(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });

    }

    private String replaceLastPart(String originString, String replacement, String toreplace) {
        int start = originString.lastIndexOf(replacement);
        StringBuilder builder = new StringBuilder();
        builder.append(originString.substring(0, start));
        builder.append(toreplace);
        builder.append(originString.substring(start + replacement.length()));
        return builder.toString();
    }

    private void transformJar(File file, File dest, Status status) {
        logger.warn("[transformJar] inputDir:" + file.getAbsolutePath() + "\noutputDir:" + dest.getAbsolutePath());
        waitableExecutor.execute(() -> {
            if (emptyRun) {
                FileUtils.copyFile(file, dest);
                return null;
            }

            bytecodeWeaver.weaveJar(file, dest);
            return null;
        });
    }

    private boolean inDuplicatedClassSafeMode() {
        return false;
    }

    protected RunVariant getRunVariant() {
        return RunVariant.ALWAYS;
    }


}
