package org.kymjs.music.utils;

import java.util.List;

import org.kymjs.music.AppManager;
import org.kymjs.music.Config;
import org.kymjs.music.bean.Music;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

/**
 * 播放器类 单例模式：封装了播放器的相关操作
 * 
 * @author kymjs
 * 
 */
public class Player {
    private static Player player = new Player();
    private MediaPlayer media;

    private int mode;
    private int playing = Config.PLAYING_STOP;

    private List<Music> list;
    private int position = 0;
    private Context context;

    private Player() {
        if (list == null || Config.changeMusicInfo) {
            list = ListData.getLocalList(AppManager.getAppManager()
                    .currentActivity());
        }
    }

    public static Player getPlayer() {
        return player;
    }

    public List<Music> getList() {
        return this.list;
    }

    public int getListPosition() {
        return this.position;
    }

    public Music getMusic() {
        Music music = null;
        if (position >= list.size()) {
            music = new Music();
            music.setArtist(Config.ARTIST);
            music.setTitle(Config.TITLE);
        } else {
            music = list.get(position);
        }
        return music;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return this.mode;
    }

    public int getPlaying() {
        return playing;
    }

    // 获取播放的音乐文件总时间长度
    public int getDuration() {
        int durat = 0;
        if (media != null) {
            durat = media.getDuration();
        }
        return durat;
    }

    // 获取当前播放音乐时间点
    public int getCurrentPosition() {
        int currentPosition = 0;
        if (media != null) {
            currentPosition = media.getCurrentPosition();
        }
        return currentPosition;
    }

    // 将音乐播放跳转到某一时间点,以毫秒为单位
    public void seekTo(int msec) {
        if (media != null) {
            media.seekTo(msec);
        }
    }

    public void destroy() {
        if (media != null) {
            media.release();
            playing = Config.PLAYING_STOP;
        }
    }

    public void stop() {
        if (playing != Config.PLAYING_STOP) {
            media.reset();
            playing = Config.PLAYING_STOP;
            context.sendBroadcast(new Intent(Config.RECEIVER_MUSIC_CHANGE));
        }
    }

    public void pause() {
        if (playing != Config.PLAYING_PAUSE) {
            media.pause();
            playing = Config.PLAYING_PAUSE;
            context.sendBroadcast(new Intent(Config.RECEIVER_MUSIC_CHANGE));
        }
    }

    // 正在暂停，即将开始继续播放
    public Music replay() {
        if (playing != Config.PLAYING_PLAY) {
            media.start();
            playing = Config.PLAYING_PLAY;
            context.sendBroadcast(new Intent(Config.RECEIVER_MUSIC_CHANGE));
        }
        return list.get(position);
    }

    public Music play(Context context, List<Music> list, int position) {
        // 如果有正在播放的歌曲，将它停止
        if (playing == Config.PLAYING_PLAY) {
            media.reset();
        }
        media = MediaPlayer.create(context,
                Uri.parse("file://" + list.get(position).getPath()));
        try {
            media.start();
            this.list = list;
            this.position = position;
            this.context = context;
            media.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Player.this.context.sendBroadcast(new Intent(
                            Config.RECEIVER_MUSIC_CHANGE));
                    completion(Player.this.context, Player.this.list,
                            Player.this.position);
                }
            });
            playing = Config.PLAYING_PLAY;
            context.sendBroadcast(new Intent(Config.RECEIVER_MUSIC_CHANGE));
        } catch (NullPointerException e) {
            UIHelper.toast("亲，找不到歌曲了，存储卡拔掉了吗？");
        }
        return list.get(position);
    }

    public Music next(Context context) {
        Music music = null;
        if (list.size() < 1) {
            this.destroy();
        } else {
            media.reset(); // 停止上一首
            position = (position + 1) % list.size();
            play(context, list, position);
            music = list.get(position);
        }
        return music;
    }

    public Music previous(Context context) {
        Music music = null;
        if (list.size() < 1) {
            this.destroy();
            music = null;
        } else {
            media.reset(); // 停止上一首
            position = (position + list.size() - 1) % list.size();
            play(context, list, position);
            music = list.get(position);
        }
        return music;
    }

    public Music completion(Context context, List<Music> list, int position) {
        Music music = null;
        switch (mode) {
        case Config.MODE_REPEAT_SINGLE:
            // 单曲播放
            stop();
            break;
        case Config.MODE_REPEAT_ALL:
            // 单曲循环
            music = play(context, list, position);
            break;
        case Config.MODE_SEQUENCE:
            // 列表循环
            music = play(context, list, (position + 1) % list.size());
            break;
        case Config.MODE_RANDOM:
            // 随机循环
            music = play(context, list, (int) (Math.random() * list.size()));
            break;
        default:
            break;
        }
        return music;
    }
}
