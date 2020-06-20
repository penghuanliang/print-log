package com.phl.print.transform.asm;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public abstract class BaseWeaver implements IWeaver {

    private static final FileTime ZERO = FileTime.fromMillis(0);
    private static final String SEPARATOR = File.separator;

    protected ClassLoader classLoader;

    public BaseWeaver() {
    }

    @Override
    public boolean isWeavableClass(String filePath) throws IOException {
        return filePath.endsWith(".class") && !filePath.contains("R$") && !filePath.contains("R.class") && !filePath.contains("BuildConfig.class");
    }

    @Override
    public byte[] weaveSingleClassToByteArray(InputStream inputStream) throws IOException {
        ClassReader classReader = new ClassReader(inputStream);
        ClassWriter classWriter = new ExtendClassWriter(classLoader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor classWriterWrapper = wrapClassWriter(classWriter);
        classReader.accept(classWriterWrapper, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

    public final void weaveJar(File inputJar, File outputJar) throws IOException {
        ZipFile inputZip = new ZipFile(inputJar);
        ZipOutputStream outputZip = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(outputJar.toPath())));
        Enumeration<? extends ZipEntry> inEntries = inputZip.entries();
        while (inEntries.hasMoreElements()) {
            ZipEntry entry = inEntries.nextElement();
            InputStream originalFile = new BufferedInputStream(inputZip.getInputStream(entry));
            ZipEntry outEntry = new ZipEntry(entry);
            byte[] newEntryContent;
            if (!isWeavableClass(outEntry.getName().replace("/", "."))) {
                newEntryContent = IOUtils.toByteArray(originalFile);
            } else {
                //wrap class byte
                newEntryContent = weaveSingleClassToByteArray(originalFile);
            }

            CRC32 crc32 = new CRC32();
            crc32.update(newEntryContent);
            outEntry.setCrc(crc32.getValue());
            outEntry.setMethod(ZipEntry.STORED);
            outEntry.setSize(newEntryContent.length);
            outEntry.setCompressedSize(newEntryContent.length);
            outEntry.setLastAccessTime(ZERO);
            outEntry.setLastModifiedTime(ZERO);
            outEntry.setCreationTime(ZERO);
            outputZip.putNextEntry(outEntry);
            outputZip.write(newEntryContent);
            outputZip.closeEntry();
        }
        outputZip.flush();
        outputZip.close();
    }

    public final void weaveSingleClassToFile(File inputFile, File outputFile, String inputBaseDir) throws IOException {
        if (!inputBaseDir.endsWith(SEPARATOR)) {
            inputBaseDir = inputBaseDir + SEPARATOR;
        }

        if (!isWeavableClass(inputFile.getAbsolutePath().replace(inputBaseDir, "").replace(SEPARATOR, "."))) {
            if (inputFile.isFile()) {
                FileUtils.touch(outputFile);
                FileUtils.copyFile(inputFile, outputFile);
            }
        } else {
            FileUtils.touch(outputFile);
            InputStream inputStream = new FileInputStream(inputFile);
            byte[] bytes = weaveSingleClassToByteArray(inputStream);
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(bytes);
            fos.flush();
            fos.close();
        }

    }


    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    protected ClassVisitor wrapClassWriter(ClassWriter classWriter) {
        return classWriter;
    }
}
