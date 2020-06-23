package com.phl.print.plugin.logline;

import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.phl.print.plugin.logline.code.LoglineWeaver;
import com.phl.print.transform.BaseTransform;
import com.phl.print.transform.RunVariant;

import org.gradle.api.Project;

import java.io.IOException;

public class LinelogTransform extends BaseTransform {

    private PrintExt printExt;

    public LinelogTransform(Project project) {
        super(project);
        this.printExt = (PrintExt) project.getExtensions().getByName("printExt");
        this.bytecodeWeaver = new LoglineWeaver();
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
    }

    @Override
    protected RunVariant getRunVariant() {
        return printExt.runVariant;
    }
}
