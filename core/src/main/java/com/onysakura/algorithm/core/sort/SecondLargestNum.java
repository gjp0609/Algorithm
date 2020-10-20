package com.onysakura.algorithm.core.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class SecondLargestNum {

    public static void main(String[] args) {
        Random random = new Random();
        int length = 10;
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt(10);
        }
        System.out.println("array: " + Arrays.toString(array));
        Integer result = fun(array);
        System.out.println("result: " + result);
        ArrayList<Integer> list = new ArrayList<>();
        for (int i : array) {
            list.add(i);
        }
        Collections.sort(list);
        System.out.println("array sorted: " + list);
    }

    /**
     * 取一个数组中第二大的数字
     */
    public static Integer fun(int[] array) {
        int newMax = Integer.MIN_VALUE;
        int lastMax = Integer.MIN_VALUE;
        for (int i : array) {
            if (i >= newMax) {
                if (i > newMax) {
                    lastMax = newMax;
                }
                newMax = i;
            } else if (i > lastMax) {
                lastMax = i;
            }
        }
        return lastMax;
    }
}
