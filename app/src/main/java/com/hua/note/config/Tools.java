package com.hua.note.config;

import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Iaovy on 2020/3/24 10:21
 *
 * @email Cymbidium@outlook.com
 */
public class Tools {
    public static void myToast(Context context, CharSequence charSequence) {
        Toast.makeText(context, charSequence, Toast.LENGTH_SHORT).show();
    }

    public static int ranColor() {
        Random random = new Random();
        return 0xff000000 | random.nextInt(0xffffffff);
    }

    /**
     * 获取星期几
     *
     * @return
     */
    public static String getWeekDays() {
        String[] weekDays = {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week < 0) {
            week = 0;
        }
        return weekDays[week];
    }

    public static boolean isWordChanged(String oldString, String newString) {
        return !oldString.equals(newString);
    }

    /**
     * 截取指定长度字符串
     *
     * @param strs
     * @param length
     * @return
     */
    public static String cutStr(String strs, int length) {
        int sum = 0;
        String finalStr = "";
        if (null == strs || strs.toCharArray().length <= length) {
            finalStr = (strs == null ? "" : strs);
        } else {
            for (int i = 0; i < strs.length(); i++) {
                String str = strs.substring(i, i + 1);
                // 累加单个字符字节数
                sum += str.toCharArray().length;
                if (sum > length) {
                    finalStr = strs.substring(0, i) + "...";
                    break;
                }
            }
        }
        return finalStr;
    }
}
