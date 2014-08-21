package org.kymjs.music.utils;

import org.kymjs.music.Config;

import android.content.Context;
import android.content.Intent;

public class ErrHandleUtils {
    public static void sendErrInfo(Context context, String errInfo) {
        Intent errIntent = new Intent();
        errIntent.setAction(Config.RECEIVER_ERROR);
        errIntent.putExtra("error", errInfo);
        context.sendBroadcast(errIntent);
    }
}
