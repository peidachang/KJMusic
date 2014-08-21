package org.kymjs.music.utils;

import java.io.File;

import android.os.Environment;

/**
 * 获得文件存储路径
 * @author kymjs
 */
public class FileUtils {
    private static final int NULL = 0;
    private static final int READ_ONLY = 1;
    private static final int READ_WRITE = 2;
    private String state = Environment.getExternalStorageState();
    private int permission;

    public File getSavePath() {
        File path = null;
        // 检查外部存储是否可用
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            permission = READ_WRITE;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            permission = READ_ONLY;
        } else {
            permission = NULL;
        }
        // 没有读写权限，直接退出
        if (permission >= READ_WRITE) {
            File pathFile = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            pathFile.mkdirs();
            path = new File(pathFile.toString() + "/lyric");
            path.mkdirs();
        }
        return path;
    }
}
