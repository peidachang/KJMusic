package org.kymjs.music;

import android.util.Log;

/**
 * 应用程序的Log管理器
 * 
 * @author kymjs
 * @version 1.0
 * @created 2013-11-24
 */
public class AppLog {
    private static final boolean IS_DEBUG = true;
    private static final boolean SHOW_ACTIVITY_STATE = true;

    public static final void debug(String msg) {
        if (IS_DEBUG) {
            Log.i("debug", msg);
        }
    }

    public static final void debug(String msg, Throwable tr) {
        if (IS_DEBUG) {
            Log.i("debug", msg, tr);
        }
    }

    public static final void kymjs(String msg) {
        if (IS_DEBUG) {
            Log.i("kymjs", msg);
        }
    }

    public static final void state(Class cls, String state) {
        if (SHOW_ACTIVITY_STATE) {
            Log.d("state", cls + state);
        }
    }
}
