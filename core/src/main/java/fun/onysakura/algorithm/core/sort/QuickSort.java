package fun.onysakura.algorithm.core.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class QuickSort {

    public static void main(String[] args) {
        int length = 20;
        Random random = new Random();
        int[] array = new int[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(90) + 10;
        }
        System.out.println("原始数组: " + Arrays.toString(array));
        fun(array);
        System.out.println("排序结果: " + Arrays.toString(array));
        ArrayList<Integer> list = new ArrayList<>();
        for (int i : array) {
            list.add(i);
        }
        Collections.sort(list);
        System.out.println("正确顺序: " + list);

    }

    /**
     * 快速排序
     */
    public static void fun(int[] array) {
        quickSort(array, 0, array.length - 1);
    }

    public static void quickSort(int[] array, int l, int r) {
        if (l < r) {
            int i = l, j = r;
            int init = array[i];
            while (i < j) {
                while (i < j && array[j] >= init) {
                    j--;
                }
                if (i < j) {
                    array[i] = array[j];
                    i++;
                }
                while (i < j && array[i] < init) {
                    i++;
                }
                if (i < j) {
                    array[j] = array[i];
                    j--;
                }
            }
            array[i] = init;
            quickSort(array, l, i - 1);
            quickSort(array, i + 1, r);
        }
    }
}
