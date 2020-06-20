package com.phl.print.plugin.logline;

import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.phl.print.plugin.logline.code.LoglineWeaver;
import com.phl.print.transform.BaseTransform;
import com.phl.print.transform.RunVariant;

import org.gradle.api.Project;

import java.io.IOException;

public class LinelogTransform extends BaseTransform {

    private Project project;
    private LinelogExtension linelogExtension;
    public static final String LINE_LOG_NAME = "linelogExt";

    public LinelogTransform(Project project) {
        super(project);
        this.project = project;
        project.getExtensions().create(LINE_LOG_NAME, LinelogExtension.class);
        this.linelogExtension = (LinelogExtension) project.getExtensions().getByName(LINE_LOG_NAME);
        this.bytecodeWeaver = new LoglineWeaver();
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
    }

    @Override
    protected RunVariant getRunVariant() {
        return linelogExtension.runVariant;
    }
}
