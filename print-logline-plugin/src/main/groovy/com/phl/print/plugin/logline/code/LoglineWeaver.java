package com.phl.print.plugin.logline.code;

import com.phl.print.transform.asm.BaseWeaver;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

public class LoglineWeaver extends BaseWeaver {

    public static final String PLUGIN_LIBRARY = "com.phl.print_logline_library";


    @Override
    public boolean isWeavableClass(String filePath) throws IOException {
        boolean superResult = super.isWeavableClass(filePath);
        boolean isByteCodePlugin = filePath.startsWith(PLUGIN_LIBRARY);
        return superResult && !isByteCodePlugin;
    }

    @Override
    protected ClassVisitor wrapClassWriter(ClassWriter classWriter) {
        return new LinelogClassAdapter(classWriter);
    }
}
