package org.kymjs.music.ui.widget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 默认的LRC生成器，将原歌词LRC行转为字符串
 */
public class DefaultLrcBuilder {
    public List<LrcRow> getLrcRows(String rawLrc) {
        if (rawLrc == null || rawLrc.length() == 0) {
            return null;
        }
        StringReader reader = new StringReader(rawLrc);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        List<LrcRow> rows = new ArrayList<LrcRow>();
        try {
            do {
                line = br.readLine();
                if (line != null && line.length() > 0) {
                    List<LrcRow> lrcRows = LrcRow.createRows(line);
                    if (lrcRows != null && lrcRows.size() > 0) {
                        for (LrcRow row : lrcRows) {
                            rows.add(row);
                        }
                    }
                }
            } while (line != null);
            if (rows.size() > 0) {
                // sort by time:
                Collections.sort(rows);
            }
        } catch (Exception e) {
            return null;
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader.close();
        }
        return rows;
    }
}
