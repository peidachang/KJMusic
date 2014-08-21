package org.kymjs.music.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.kymjs.music.bean.Music;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * 杂项工具类
 * 
 * @author kymjs
 * 
 */
public class Tool {
    /**
     * 设置铃声
     */
    public static final void setRingtone(Context context, Music whichMusic) {
        Uri ringtoneUri = Uri.parse("file://" + whichMusic.getPath());
        RingtoneManager.setActualDefaultRingtoneUri(context,
                RingtoneManager.TYPE_RINGTONE, ringtoneUri);
        UIHelper.toast(context, "已将歌曲设置为来电铃声");
    }

    /**
     * 分享给好友
     * 
     * @param context
     */
    public static final void shareToFriend(Context context) {
    }

    /**
     * 返回当前系统时间
     * 
     * @return
     */
    public static String getDataTime() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(new Date());
    }

    /**
     * 判断网络是否连接
     * 
     * @param context
     * @return
     */
    public static boolean isCheckNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info == null) ? false : true; // 网络是否连接
    }
}
