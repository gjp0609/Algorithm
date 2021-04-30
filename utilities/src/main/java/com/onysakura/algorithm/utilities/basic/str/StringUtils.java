package com.onysakura.algorithm.utilities.basic.str;

import com.onysakura.algorithm.utilities.basic.MathUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
public class StringUtils {

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(CharSequence sequence) {
        return org.apache.commons.lang3.StringUtils.isEmpty(sequence);
    }

    /**
     * 判断字符串是否为空白字符串
     */
    public static boolean isBlank(CharSequence sequence) {
        return org.apache.commons.lang3.StringUtils.isBlank(sequence);
    }

    /**
     * 比较字符串是否相等，其中一个为 null 则返回 false
     */
    public static boolean equals(String a, String b) {
        return a != null && a.equals(b);
    }

    /**
     * 比较字符串是否相等，忽略大小写，其中一个为 null 则返回 false
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return a != null && a.equalsIgnoreCase(b);
    }

    /**
     * 驼峰转下划线
     */
    public static String humpToUnderline(String para) {
        StringBuffer sb = new StringBuffer(para);
        int temp = 0;
        if (!para.contains("_")) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 下划线转驼峰
     */
    public static String underlineToHump(String para) {
        StringBuffer result = new StringBuffer();
        String[] a = para.split("_");
        for (String s : a) {
            if (!para.contains("_")) {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 计算两个字符串的相似度
     */
    public static int levenshtein(String str1, String str2) {
        //计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][] dif = new int[len1 + 1][len2 + 1];
        for (int a = 0; a <= len1; a++) {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] = a;
        }
        //计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                dif[i][j] = MathUtils.min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1, dif[i - 1][j] + 1);
            }
        }
        log.debug("字符串[" + str1 + "]与[" + str2 + "]的比较");
        //取数组右下角的值，同样不同位置代表不同字符串的比较
        log.debug("差异步骤：" + dif[len1][len2]);
        float v = 1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
        log.debug("相似度：" + v);
        //计算相似度
        return (int) (v * 100);
    }
}
