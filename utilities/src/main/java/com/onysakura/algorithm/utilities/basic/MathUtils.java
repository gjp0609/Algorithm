package com.onysakura.algorithm.utilities.basic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
public class MathUtils {

    /**
     * 取最小值
     *
     * @param ints 数字
     */
    public static int min(int... ints) {
        int min = Integer.MAX_VALUE;
        for (int i : ints) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }

    /**
     * 取最大值
     *
     * @param ints 数字
     */
    public static int max(int... ints) {
        int max = Integer.MIN_VALUE;
        for (int i : ints) {
            if (max < i) {
                max = i;
            }
        }
        return max;
    }
}
