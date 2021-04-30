package com.onysakura.algorithm.utilities.web;

import com.onysakura.algorithm.utilities.basic.str.StringUtils;

import java.util.*;

@SuppressWarnings("unused")
public class ParamsUtils {

    private static final String EQUAL = "=";
    private static final String DELIMITER = "&";

    /**
     * 将待加密的参数组装为字符串
     *
     * @param map 参数
     */
    public static String join(Map<String, String> map) {
        return join(map, false);
    }

    /***
     * 将待加密的参数组装为字符串
     * @param map 参数
     * @param ignoreBlankValue 是否忽略空值
     */
    public static String join(Map<String, String> map, boolean ignoreBlankValue) {
        return join(map, ignoreBlankValue, EQUAL, DELIMITER);
    }

    /**
     * 将待加密的参数组装为字符串
     *
     * @param map              参数
     * @param ignoreBlankValue 是否忽略空值
     * @param equal            key 和 value 的连接符
     * @param delimiter        参数之间的连接符
     */
    public static String join(Map<String, String> map, boolean ignoreBlankValue, String equal, String delimiter) {
        ArrayList<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (ignoreBlankValue) {
                if (StringUtils.isBlank(value)) {
                    continue;
                }
            }
            String s = key + equal + value;
            list.add(s);
        }
        Collections.sort(list);
        return String.join(delimiter, list);
    }

    /**
     * 除去数组中的空值和签名参数
     *
     * @param params       签名参数对象
     * @param removeEmpty  是否移除空值
     * @param ignoreParams 忽略的 key
     */
    public static Map<String, String> paramFilter(Map<String, String> params, boolean removeEmpty, String... ignoreParams) {
        Map<String, String> result = new HashMap<>();
        if (params == null || params.size() <= 0) {
            return result;
        }
        key:
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (removeEmpty && StringUtils.isBlank(value)) {
                continue;
            }
            for (String ignoreParam : ignoreParams) {
                if (StringUtils.equals(key, ignoreParam)) {
                    continue key;
                }
            }
            result.put(key, value);
        }
        return result;
    }
}
