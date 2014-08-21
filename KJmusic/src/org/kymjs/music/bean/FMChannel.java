package org.kymjs.music.bean;

/**
 * 豆瓣FM 频道信息
 * 
 * @author kymjs
 * @请求接口：http://www.douban.com/j/app/radio/channels
 */
public class FMChannel {
    private int id;
    private String name;
    private String channelId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
