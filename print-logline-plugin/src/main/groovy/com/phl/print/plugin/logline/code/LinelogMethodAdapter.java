package com.phl.print.plugin.logline.code;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LinelogMethodAdapter extends MethodVisitor implements Opcodes {

    private int lineNumber;

    public LinelogMethodAdapter(MethodVisitor methodVisitor) {
        super(ASM5, methodVisitor);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        this.lineNumber = line;
        super.visitLineNumber(line, start);
    }


    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean isInterface) {
        if ("android/util/Log".equals(owner)) {
            String linenumberConst = lineNumber + "";
            if ("i".equals(name)) {
                if ("(Ljava/lang/String;Ljava/lang/String;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "i", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I", false);
                } else if ("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "i", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)I", false);
                } else {
                    super.visitMethodInsn(opcode, owner, name, desc, isInterface);
                }
            } else if ("d".equals(name)) {
                if ("(Ljava/lang/String;Ljava/lang/String;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "d", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I", false);
                } else if ("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "d", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)I", false);
                } else {
                    super.visitMethodInsn(opcode, owner, name, desc, isInterface);
                }
            } else if ("v".equals(name)) {
                if ("(Ljava/lang/String;Ljava/lang/String;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "v", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I", false);
                } else if ("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "v", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)I", false);
                } else {
                    super.visitMethodInsn(opcode, owner, name, desc, isInterface);
                }
            } else if ("e".equals(name)) {
                if ("(Ljava/lang/String;Ljava/lang/String;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "e", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I", false);
                } else if ("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "e", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)I", false);
                } else {
                    super.visitMethodInsn(opcode, owner, name, desc, isInterface);
                }
            } else if ("w".equals(name)) {
                if ("(Ljava/lang/String;Ljava/lang/String;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "w", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I", false);
                } else if ("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "w", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)I", false);
                } else if ("(Ljava/lang/String;Ljava/lang/Throwable;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "w", "(Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)I", false);
                } else {
                    super.visitMethodInsn(opcode, owner, name, desc, isInterface);
                }
            } else if ("println".equals(name)) {
                if ("(ILjava/lang/String;Ljava/lang/String;)I".equals(desc)) {
                    mv.visitLdcInsn(linenumberConst);
                    mv.visitMethodInsn(INVOKESTATIC, "com/phl/print_logline_library/LineNumberLog", "println", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)I", false);
                } else {
                    super.visitMethodInsn(opcode, owner, name, desc, isInterface);
                }
            } else {
                super.visitMethodInsn(opcode, owner, name, desc, isInterface);
            }
        }else{
            super.visitMethodInsn(opcode, owner, name, desc, isInterface);
        }
    }
}
