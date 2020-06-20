package com.phl.print.plugin.logline;

import com.phl.print.transform.RunVariant;

public class LinelogExtension {
    public RunVariant runVariant = RunVariant.ALWAYS;
    public boolean duplcatedClassSafeMode = false;

    @Override
    public String toString() {
        return "LinelogExtension{" +
                "runVariant=" + runVariant +
                ", duplcatedClassSafeMode=" + duplcatedClassSafeMode +
                '}';
    }
}
