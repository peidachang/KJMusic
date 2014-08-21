package org.kymjs.music.parser;

import org.kymjs.music.AppLog;
import org.kymjs.music.bean.Music;
import org.kymjs.music.utils.ErrHandleUtils;

import android.content.Context;

public class ParserMusicXML {
    public static Music ParserMusic(Music music, String xml) {
        // music.setDecode("http://zhangmenshiting.baidu.com/data2/music/64380827/Z2ZmbGVuaW9fn6NndK6ap5WXcGZtbJxvaGlkZGaZaJpplmxnZmxpmmRrZmlpam5rZWWVmZaXa3CTZJeZmW1ncGVll1qin5t1YWBnbG1raHBlZmtmbGxvcTE$");
        // music.setEncode("64380827.mp3?xcode=286e777101c1b8d93162b3935447343beaa48b2ded70843d&mid=0.48851844827689");
        // music.setLrcid(14706 + "");
        try {
            String encode = null;
            String decode = null;
            String lrcid = null;
            int begin = 0, end = 0;

            // 偷个懒，不用xml解析
            begin = xml.indexOf("<encode>") + 8;
            end = xml.indexOf("</encode>");
            if (begin > 0 && end > begin) {
                encode = xml.substring(begin, end);
            }
            begin = xml.indexOf("<decode>") + 8;
            end = xml.indexOf("</decode>");
            if (begin > 0 && end > begin) {
                decode = xml.substring(begin, end);
            }
            begin = xml.indexOf("<lrcid>") + 7;
            end = xml.indexOf("</lrcid>");
            if (begin > 0 && end > begin) {
                lrcid = xml.substring(begin, end);
            }

            encode = encode.substring(9, encode.length() - 3);
            music.setEncode(encode);
            decode = decode.substring(9, decode.length() - 3);
            music.setDecode(decode);
            music.setLrcId(lrcid);
        } catch (Exception e) {
            AppLog.debug("xml字串异常，无法解析");
            music.setLrcId("0000");
            return music;
        }
        return music;
    }

    // http://image.baidu.com/i?tn=baiduimagejson&ie=utf-8&ic=0&rn=20&pn=1&word=
    public static String ParserMusicImg(Context context,String xml, int i) {
        String imgUrl = xml;
        for (int count = 0; count <= i; count++) {
            int begin = 0, end = 0;
            begin = xml.indexOf("\"objURL\":\"") + 10;
            end = xml.indexOf("\"", begin);
            if (begin < 0 || end < 0) {
                ErrHandleUtils.sendErrInfo(context, "没有更多图片了");
                break;
            } else {
                imgUrl = xml.substring(begin, end);
            }
            xml = xml.substring(end);
        }
        return imgUrl;
    }
}
