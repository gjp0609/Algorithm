package com.onysakura.algorithm.logic;

import java.util.ArrayList;
import java.util.Collections;

public class BaseNCalculator {
    // 进制基础数据
    private static final String BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ字也神中制行源文进数";
    private static final boolean logEnable = false;

    /**
     * 任意进制数转换
     */
    public static void main(String[] args) {
        System.out.println(trans("10001010", 2, 7));
        System.out.println(trans("22637577", 10, 70));
    }


    /**
     * 任意进制数转换
     *
     * @param source      源数字
     * @param sourceBaseN 源数字进制
     * @param targetBaseN 目标进制数
     */
    public static String trans(String source, int sourceBaseN, int targetBaseN) {
        if (targetBaseN > BASE.length() || sourceBaseN > BASE.length()) throw new RuntimeException("error");
        long decimal = toDecimal(source, sourceBaseN);
        String baseN = toBaseN(decimal, targetBaseN);
        if (logEnable) {
            System.out.println(source + " -> " + decimal + " -> " + baseN);
        }
        return baseN;
    }

    /**
     * 将数字转换为十进制，便于计算
     *
     * @param source      源数字
     * @param sourceBaseN 源数字进制
     * @return 十进制的值
     */
    public static long toDecimal(String source, int sourceBaseN) {
        String[] split = source.split("");
        int length = split.length;
        long result = 0L;
        for (int i = length - 1; i >= 0; i--) {
            int level = length - 1 - i;
            int num = BASE.indexOf(split[i]);
            if (num + 1 > sourceBaseN) throw new RuntimeException("error");
            double v = num * Math.pow(sourceBaseN, level);
            if (logEnable) {
                System.out.println(num + " * " + sourceBaseN + "^" + level + " = " + Double.valueOf(v).longValue());
            }
            result += v;
        }
        return result;
    }

    /**
     * 将十进制数据转为指定进制数据
     *
     * @param source      源数字
     * @param targetBaseN 指定的进制
     * @return 指定进制数字
     */
    public static String toBaseN(long source, int targetBaseN) {
        ArrayList<String> list = new ArrayList<>();
        long res;
        do {
            res = source / targetBaseN;
            long mod = source % targetBaseN;
            list.add(String.valueOf(BASE.charAt((int) mod)));
            source = res;
        } while (res > 0);
        if (logEnable) {
            System.out.print(list);
        }
        Collections.reverse(list);
        String result = String.join("", list);
        if (logEnable) {
            System.out.println(" -> " + list + " -> " + result);
        }
        return result;
    }
}
