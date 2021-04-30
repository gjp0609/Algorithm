package com.onysakura.algorithm.utilities.basic;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Date;

@Slf4j
@SuppressWarnings("unused")
public class DateUtils {

    /**
     * 格式化日期
     *
     * @param date    日期
     * @param pattern 格式
     */
    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 解析日期
     *
     * @param date    字符串日期
     * @param pattern 格式
     */
    public static Date parse(String date, String pattern) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(date, pattern);
        } catch (ParseException e) {
            log.warn("解析日期失败", e);
            return null;
        }
    }
}
