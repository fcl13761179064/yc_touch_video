package com.common.framework.utils.handler;

import android.os.Handler;
import android.os.Looper;

public final class HandlerUIRun {

    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    public static void runUI(Runnable task) {
        if (task != null) {
            uiHandler.post(task);
        }
    }

    public static void runNewUIDelayed(Runnable task, long delayMillis) {
        if (task != null) {
            uiHandler.postDelayed(task, delayMillis);
        }
    }

    public static void runNewUI(Runnable task) {
        if (task != null) {
            uiHandler.post(task);
        }
    }

    public static void clearUI() {
        uiHandler.removeCallbacksAndMessages(null);
    }
}
