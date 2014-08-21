package org.kymjs.music.service;

import net.tsz.afinal.FinalDb;

import org.kymjs.music.AppLog;
import org.kymjs.music.Config;
import org.kymjs.music.bean.Music;
import org.kymjs.music.utils.StringUtils;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * 从本地媒体库扫描音乐并加入数据库
 * 
 * @author kymjs
 */
public class ScanMusic extends IntentService {
    private int count;

    public ScanMusic() {
        super("org.kymjs.music.service.ScanMusic");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean result = false;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        if (cursor == null) {
            result = false;
        } else {
            FinalDb db = FinalDb.create(this, Config.DB_NAME, Config.isDebug);
            db.deleteByWhere(Music.class, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                // String id = cursor.getString(cursor
                // .getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DATA));
                String size = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.SIZE));
                if (StringUtils.toInt(size, 0) > 700 * 1024) {
                    Music music = new Music();
                    music.setTitle(title);
                    music.setArtist(artist);
                    music.setPath(path);
                    music.setSize(size);
                    music.setCollect(0);
                    music.setDecode("");
                    music.setEncode("");
                    music.setLrcId("");
                    db.save(music);
                    count++;
                    AppLog.debug("找到音乐：" + music.getTitle());
                }
            }
            result = true;
            Config.changeMusicInfo = true;
            Config.changeCollectInfo = true;
            cursor.close();
        }

        // 发送扫描成功或失败的广播
        Intent musicScan = new Intent();
        String action = result ? Config.RECEIVER_UPDATE_MUSIC_LIST
                : Config.RECEIVER_MUSIC_SCAN_FAIL;
        musicScan.setAction(action);
        musicScan.putExtra(Config.SCAN_MUSIC_COUNT, count);
        sendBroadcast(musicScan);
    }
}
