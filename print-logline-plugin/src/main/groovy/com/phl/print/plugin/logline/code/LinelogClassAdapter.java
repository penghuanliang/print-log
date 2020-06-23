package com.phl.print.plugin.logline.code;

import com.phl.print.plugin.logline.PrintExtUtil;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LinelogClassAdapter extends ClassVisitor implements Opcodes {

    private String className;

    public LinelogClassAdapter(ClassVisitor classVisitor) {
        super(ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if (PrintExtUtil.getInstance().ignorePackageNames(className)) {
            return mv;
        }
        return null == mv ? null : new LinelogMethodAdapter(mv);
    }
}
