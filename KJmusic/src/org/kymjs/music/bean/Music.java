package org.kymjs.music.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import org.kymjs.music.utils.StringUtils;

/**
 * 本地音乐文件bean类
 */
@Table(name = "music")
public class Music implements Serializable {
    // 设置自定义主键
    @Id(column = "id")
    private int id;
    private String title;
    private String artist;
    private String path;
    private String size;
    private int collect;
    private String encode;
    private String decode;
    private String lrcId;
    private String lrcUrl;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLrcId() {
        return lrcId;
    }

    public void setLrcId(String lrcId) {
        this.lrcId = lrcId;
        int lrcid = StringUtils.toInt(this.lrcId, 0);
        lrcUrl = "http://box.zhangmen.baidu.com/bdlrc/" + (lrcid / 100) + "/"
                + lrcid + ".lrc";
    }

    public String getLrcUrl() {
        return lrcUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getDecode() {
        return decode;
    }

    public void setDecode(String decode) {
        this.decode = decode;
    }

    public void setLrcUrl(String lrcUrl) {
        this.lrcUrl = lrcUrl;
    }
}
