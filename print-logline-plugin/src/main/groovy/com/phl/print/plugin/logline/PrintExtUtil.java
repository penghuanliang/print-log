package com.phl.print.plugin.logline;

import java.util.ArrayList;
import java.util.List;

public class PrintExtUtil {

    private List<String> ignoreClassList = new ArrayList<>();

    private static class Holder {
        public static final PrintExtUtil INSTANCE = new PrintExtUtil();
    }

    public static PrintExtUtil getInstance() {
        return Holder.INSTANCE;
    }


    public void init(PrintExt printExt) {
        ignoreClassList.clear();
        setIgnoreClass(printExt.ignoreClass);
    }

    private void setIgnoreClass(List<String> ignoreClassList) {
        if (ignoreClassList.isEmpty()) {
            return;
        }
        this.ignoreClassList.clear();
        for (String ignoreClass : ignoreClassList) {
            this.ignoreClassList.add(ignoreClass.replaceAll("\\.", "/"));
        }
    }


    public boolean ignorePackageNames(String className) {
        boolean isMatched = false;
        for (String packageName : ignoreClassList) {
            if (className.contains(packageName)) {
                isMatched = true;
                break;
            }
        }
        return isMatched;
    }
}
