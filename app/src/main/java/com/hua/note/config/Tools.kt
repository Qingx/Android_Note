package com.hua.note.config

import android.content.Context
import android.widget.Toast
import cn.wl.android.lib.utils.Times
import java.util.*

/**
 * Created by Iaovy on 2020/4/1 11:13
 *@email Cymbidium@outlook.com
 */
class Tools {

    companion object {
        /**
         * 简化的Toast
         */
        fun myToast(context: Context?, charSequence: CharSequence?) {
            Toast.makeText(context, charSequence, Toast.LENGTH_SHORT).show()
        }

        /**
         * 随机颜色
         */
        fun ranColor(): Int {
            val random = Random()
            return -0x1000000 or random.nextInt(-0x1)
        }

        /**
         * 获取星期几
         */
        fun getWeekDays(): String? {
            val weekDays =
                arrayOf("星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
            val calendar = Calendar.getInstance()
            var week = calendar[Calendar.DAY_OF_WEEK] - 1
            if (week < 0) {
                week = 0
            }
            return weekDays[week]
        }

        /**
         * 判断文字是否改变
         */
        fun isWordChanged(oldString: String, newString: String): Boolean {
            return oldString != newString
        }

        /**
         * 截取字符串长度
         */
        fun cutStr(strs: String?, length: Int): String? {
            var sum = 0
            var finalStr = ""
            if (null == strs || strs.toCharArray().size <= length) {
                finalStr = strs ?: ""
            } else {
                for (i in strs.indices) {
                    val str = strs.substring(i, i + 1)
                    // 累加单个字符字节数
                    sum += str.toCharArray().size
                    if (sum > length) {
                        finalStr = strs.substring(0, i) + "..."
                        break
                    }
                }
            }
            return finalStr
        }

        /**
         * 获取年月日天时分星期
         * E: 2020年03月20日 21:52 星期一
         *
         * @return
         */
        fun getYMDTW(): String? {
            val date = DateFormat.yearMonthDayTime(Times.current())
            val weekDay = getWeekDays()
            return "$date $weekDay"
        }
    }
}