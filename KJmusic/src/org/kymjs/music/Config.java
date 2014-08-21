package org.kymjs.music;

public class Config {
    /** 当前版本 */
    public static final double VEWSION = 1.0;
    
    /**
     * Operation collect list's popupWindow
     */
    public static final float Width = 252;
    public static final float Height = 59.5F;

    /** Default display */
    public static final String TITLE = "KJ音乐";
    public static final String ARTIST = "kymJS";

    /** 应用是否首次安装 */
    public static final String FIRSTINSTALL_FILE = "first";
    public static final String FIRSTINSTALL_KEY = "firstinstall";

    /** 数据库名字 */
    public static final String DB_NAME = "kymjs_music_db";
    /** 是否启动调试模式 */
    public static final boolean isDebug = true;

    /** 播放列表循环模式 */
    public static final int MODE_REPEAT_SINGLE = 0;
    public static final int MODE_REPEAT_ALL = 1;
    public static final int MODE_SEQUENCE = 2;
    public static final int MODE_RANDOM = 3;
    /** 播放列表循环模式本地存储 */
    public static final String LOOP_MODE_FILE = "loop_mode_file";
    public static final String LOOP_MODE_KEY = "loop_mode_key";

    /** 播放器状态 */
    public static final int PLAYING_STOP = 0;
    public static final int PLAYING_PAUSE = 1;
    public static final int PLAYING_PLAY = 2;

    /** 音乐改变的广播 */
    public static final String RECEIVER_MUSIC_CHANGE = "net.kymjs.music.music_change";
    /** 歌曲扫描完成广播 */
    public static final String RECEIVER_UPDATE_MUSIC_LIST = "net.kymjs.music.music_scan_success";
    public static final String RECEIVER_MUSIC_SCAN_FAIL = "net.kymjs.music.music_scan_fail";

    /** 音乐列表信息被改变 */
    public static boolean changeMusicInfo = false;
    public static boolean changeCollectInfo = false;

    /** 歌词默认显示文字 */
    public static final String LRC_TEXT = "点击搜索";
    /** 是否自动加载歌词 */
    public static final boolean isAuto = true;

    /** 下载歌曲信息xml的广播 */
    public static final String RECEIVER_DOWNLOAD_XML = "net.kymjs.music.download.xml";
    /** 下载歌词的广播 */
    public static final String RECEIVER_DOWNLOAD_LYRIC = "net.kymjs.music.download.lyric";

    /** 由于在intentService中无法显示toast */
    public static final String RECEIVER_ERROR = "net.kymjs.music.error";

    /** 更换图片 */
    public static final String CHANGE_IMG_FILE = "change_img";
    public static final String CHANGE_IMG_KEY = "change_img";

    /** 扫描到的歌曲数量 */
    public static final String SCAN_MUSIC_COUNT = "SCAN_MUSIC_COUNT";
}
