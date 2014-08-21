package org.kymjs.music.utils;

import java.util.List;

import org.kymjs.music.Config;
import org.kymjs.music.bean.Music;

import net.tsz.afinal.FinalDb;
import android.content.Context;

/**
 * 音乐列表数据
 * 
 * @author kymjs
 * 
 */
public class ListData {
    // 本地列表
    private static List<Music> localList;
    // 收藏列表
    private static List<Music> collectList;

    private static void refresh(Context context) {
        FinalDb db = FinalDb.create(context, Config.DB_NAME, Config.isDebug);
        if (localList == null || Config.changeMusicInfo) {
            localList = db.findAll(Music.class);
            Config.changeMusicInfo = false;
        }
        if (collectList == null || Config.changeCollectInfo) {
            collectList = db.findAllByWhere(Music.class, "collect = 1");
            Config.changeCollectInfo = false;
        }
    }

    public static List<Music> getLocalList(Context context) {
        refresh(context);
        return localList;
    }

    public static List<Music> getCollectList(Context context) {
        refresh(context);
        return collectList;
    }
}
