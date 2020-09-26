package com.example.tvmusicplayer.util

import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType

class PinyinUtil {
    companion object {
        /**
         * 获取一个字符串的首个字符所代表的拼音，或者'#'
         * */
        fun getHeaderLetter(s: String): Char {
            val format = HanyuPinyinOutputFormat()
            //输出设置、大小写、音标等
            format.caseType = HanyuPinyinCaseType.UPPERCASE
            format.toneType = HanyuPinyinToneType.WITHOUT_TONE
            format.vCharType = HanyuPinyinVCharType.WITH_V

            //如果长度大于0
            if (s.isNotEmpty()) {
                //如果首个字符是字母
                if (s[0] in 'a'..'z' || s[0] in 'A'..'Z') {
                    return Character.toUpperCase(s[0])
                    //如果首个字符是汉字
                } else if (s.substring(0, 1).matches(Regex("[\\u4E00-\\u9FA5]+"))) {
                    return Character.toUpperCase(PinyinHelper.toHanyuPinyinStringArray(s[0])[0][0])
                    //首个字母既不是字母、又不是汉字
                } else {
                    return '#'
                }
                //小于等于0直接返回'#'
            } else {
                return '#'
            }
        }
    }
}