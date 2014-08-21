package org.kymjs.music.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.kymjs.music.Config;
import org.kymjs.music.R;
import org.kymjs.music.parser.ParserMusicXML;

import android.content.Context;
import android.widget.ImageView;

/**
 * 图片帮助类
 * 
 * @author kymjs
 */
public class ImageUtils {
    /**
     * 为ImageView设置背景
     */
    public static void setNetBg(final Context context, final ImageView iv,
            String word) {
        setNetBg(context, iv, word, 0);
    }

    /**
     * 为ImageView设置背景
     */
    public static void setNetBg(final Context context, final ImageView iv,
            String word, final int i) {
        try {
            word = "http://image.baidu.com/i?tn=baiduimagejson&ie=utf-8&ic=0&rn=20&pn="
                    + 1 + "&word=" + URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        FinalHttp fh = new FinalHttp();
        fh.get(word, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                FinalBitmap fb = FinalBitmap.create(context);
                String u = ParserMusicXML.ParserMusicImg(context, t, i);
                fb.display(iv, u);
            }
        });
    }

    /**
     * 获取歌词界面播放按钮背景
     */
    public static int getBtnPlayBg() {
        int background = 0;
        if (Player.getPlayer().getPlaying() == Config.PLAYING_PLAY) {
            background = R.drawable.selector_radio_pause;
        } else {
            background = R.drawable.selector_radio_play;
        }
        return background;
    }

    /**
     * 获取歌曲界面播放按钮背景
     */
    public static int getBtnMusicPlayBg() {
        int background = 0;
        if (Player.getPlayer().getPlaying() == Config.PLAYING_PLAY) {
            background = R.drawable.selector_btn_pause;
        } else {
            background = R.drawable.selector_btn_play;
        }
        return background;
    }

    /**
     * 获取收藏按钮背景
     */
    public static int getBtnCollectBg(boolean isCollect) {
        return isCollect ? R.drawable.selector_lrc_collected
                : R.drawable.selector_lrc_collect;
    }

    private static final int[] loopModes = {
            R.drawable.bt_playing_mode_singlecycle,
            R.drawable.bt_playing_mode_order, R.drawable.bt_playing_mode_cycle,
            R.drawable.bt_playing_mode_shuffle };

    /**
     * 获取循环播放控件背景
     */
    public static int getImgLoopBg(Context context) {
        int loopMode = PreferenceHelper.readInt(context, Config.LOOP_MODE_FILE,
                Config.LOOP_MODE_KEY, Config.MODE_REPEAT_ALL);
        Player.getPlayer().setMode(loopMode);
        return loopModes[loopMode];
    }
}
