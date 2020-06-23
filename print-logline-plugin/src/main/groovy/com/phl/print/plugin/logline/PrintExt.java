package com.phl.print.plugin.logline;

import com.phl.print.transform.RunVariant;

import java.util.ArrayList;
import java.util.List;

public class PrintExt {

    public RunVariant runVariant = RunVariant.ALWAYS;
    public boolean duplcatedClassSafeMode = false;
    public List<String> ignoreClass = new ArrayList<>();

    @Override
    public String toString() {
        return "PrintExt{" +
                "runVariant=" + runVariant +
                ", duplcatedClassSafeMode=" + duplcatedClassSafeMode +
                ", ignoreClass=" + ignoreClass +
                '}';
    }
}
