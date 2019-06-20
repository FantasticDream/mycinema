package com.example.hello.util;

import com.example.hello.domain.Lyric;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 解析歌词
 */
public class LyricUtil {


    private static ArrayList<Lyric> lyrics;

    private static boolean isExistsLyric = false;

    public static boolean isExistsLyric() {
        return isExistsLyric;
    }

    /**
     * 得到解析歌词
     * @return
     */
    public static ArrayList<Lyric> getLyrics() {
        return lyrics;
    }

    /**
     * 读取歌词文件
     * @param file
     */
    public static void readLyricFile(File file){
        if (file == null || !file.exists()){
            lyrics = null;
            isExistsLyric = false;
        }else{
            // 解析歌词
            lyrics = new ArrayList<>();
            isExistsLyric = true;
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), getCharset(file)));
                String line = "";
                while ((line = reader.readLine()) != null){
                    parseLyric(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 排序
            Collections.sort(lyrics, new Comparator<Lyric>() {
                @Override
                public int compare(Lyric o1, Lyric o2) {
                    if (o1.getTimePoint() < o2.getTimePoint()){
                        return -1;
                    }else if (o1.getTimePoint() > o2.getTimePoint()){
                        return 1;
                    }else{
                        return 0;
                    }
                }
            });
            // 计算高亮时间
            for (int i = 0; i < lyrics.size(); i++){
                Lyric oneLyric = lyrics.get(i);
                if (i+1 < lyrics.size()){
                    Lyric twoLyric = lyrics.get(i + 1);
                    oneLyric.setSleepTime(twoLyric.getTimePoint() - oneLyric.getTimePoint());
                }
            }
        }
    }

    private static String getCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    /**
     * 解析歌词
     * @param line [02:04.12][03:37.32][00:59.73]我在这里欢笑
     * @return
     */
    private static String parseLyric(String line) {
        int pos1 = line.indexOf("[");
        int pos2 = line.indexOf("]");
        if (pos1 == 0 && pos2 != -1){
            // 装时间
            long[] times = new long[getCountTag(line)];
            String strTime = line.substring(pos1+1, pos2);
            times[0] = strTime2LongTime(strTime);
            String content = line;
            int i = 1;
            while (pos1 == 0 && pos2 != -1){
                content = content.substring(pos2 + 1);
                pos1 = content.indexOf("[");
                pos2 = content.indexOf("]");
                if (pos2 != -1){
                    strTime = content.substring(pos1+1, pos2);
                    times[i] = strTime2LongTime(strTime);
                    if (times[i] == -1){
                        return "";
                    }
                    i++;
                }
            }
            Lyric lyric = new Lyric();
            for (int j = 0; j < times.length; j++){
                if (times[j] != 0){
                    lyric.setContent(content);
                    lyric.setTimePoint(times[j]);
                    lyrics.add(lyric);
                    lyric = new Lyric();
                }
            }
            return content;
        }
        return "";
    }

    /**
     * 把String类型的时间转为long类型
     * @param strTime
     * @return
     */
    private static long strTime2LongTime(String strTime) {

        long result = -1;
        try {
            String[] s1 = strTime.split(":");
            String[] s2 = s1[1].split("\\.");

            long min = Long.parseLong(s1[0]);
            long second = Long.parseLong(s2[0]);
            long mill = Long.parseLong(s2[1]);

            result = min * 60 * 1000 + second * 1000 + mill * 10;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    /**
     * 判断有多少句歌词
     * @param line
     * @return
     */
    private static int getCountTag(String line) {
        int result = -1;
        String[] left = line.split("\\[");
        String[] right = line.split("\\]");
        if(left.length == 0 && right.length == 0){
            result = 1;
        }else if (left.length > right.length){
            result =  left.length;
        }else{
            result = right.length;
        }
        return result;
    }


}
