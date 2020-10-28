package com.onysakura.algorithm.leetcode._4;

import com.onysakura.algorithm.leetcode.Benchmark;

import java.util.*;

/**
 * 寻找两个正序数组的中位数
 * <a href="https://leetcode-cn.com/problems/median-of-two-sorted-arrays/">link</a>
 * <p>
 * 给定两个大小为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的中位数。
 * <p>
 * 进阶：你能设计一个时间复杂度为 O(log (m+n)) 的算法解决此问题吗？
 * <p>
 * 示例 1：
 * 输入：nums1 = [1,3], nums2 = [2]
 * 输出：2.00000
 * 解释：合并数组 = [1,2,3] ，中位数 2
 * <p>
 * 示例 2：
 * 输入：nums1 = [1,2], nums2 = [3,4]
 * 输出：2.50000
 * 解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) / 2 = 2.5
 * <p>
 * 示例 3：
 * 输入：nums1 = [0,0], nums2 = [0,0]
 * 输出：0.00000
 * <p>
 * 示例 4：
 * 输入：nums1 = [], nums2 = [1]
 * 输出：1.00000
 * <p>
 * 示例 5：
 * 输入：nums1 = [2], nums2 = []
 * 输出：2.00000
 * <p>
 * 提示：
 * nums1.length == m
 * nums2.length == n
 * 0 <= m <= 1000
 * 0 <= n <= 1000
 * 1 <= m + n <= 2000
 * -10^6 <= nums1[i], nums2[i] <= 10^6
 */
public class Main {

    public static void main(String[] args) {
        Benchmark.init();
        Benchmark.begin();
        double result = findMedianSortedArrays(new int[]{1, 4}, new int[]{2, 3});
        Benchmark.end();
        System.out.println(result);
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int[] array = new int[nums1.length + nums2.length];
        System.arraycopy(nums1, 0, array, 0, nums1.length);
        System.arraycopy(nums2, 0, array, nums1.length, nums2.length);
        Arrays.sort(array);
        if (array.length == 1) {
            return array[0];
        } else if (array.length % 2 == 0) {
            return (array[array.length / 2 - 1] + array[array.length / 2]) / 2D;
        } else {
            return array[array.length / 2];
        }
    }

}
