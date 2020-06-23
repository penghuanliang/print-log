package com.phl.androidlogprint.util;

import android.util.Log;

public class PrintUtil {

    public static final String TAG = PrintUtil.class.getSimpleName();

    public static void d(Object... msg) {
        log(Log.DEBUG, null, TAG, msg);
    }

    public static void d(String tag, String msg) {
        log(Log.DEBUG, null, tag, msg);
    }

    public static void e(String msg) {
        log(Log.ERROR, null, TAG, msg);
    }

    public static void e(String tag, String msg) {
        log(Log.ERROR, null, tag, msg);
    }

    public static void e(String tag, String msg,Throwable e) {
        log(Log.ERROR, e, tag, msg);
    }


    private static void log(int priority, Throwable e, String tag, Object... args) {
        StringBuilder log = new StringBuilder();
        if (null == e) {
            if (args != null && args.length > 0) {
                for (Object obj : args) {
                    log.append(obj);
                }
            }
        } else {
            String message = e.getMessage();
            String body = Log.getStackTraceString(e);
            log.append(String.format("%1$s\n%2$s", message, body));
        }

        Log.println(priority, tag, log.toString());
    }


}
