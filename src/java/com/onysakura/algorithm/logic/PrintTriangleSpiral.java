package com.onysakura.algorithm.logic;

public class PrintTriangleSpiral {

    /**
     * 打印出螺旋数字三角形
     * 1
     * 2 12
     * 3 13 11
     * 4 14 15 10
     * 5  6  7  8  9
     */
    public static void main(String[] args) {
        三角螺旋宝塔(10);
    }

    /**
     * 三角螺旋宝塔
     *
     * @param length 塔层数
     */
    public static void 三角螺旋宝塔(int length) {
        int[][] ints = new int[length][length];
        int all = 0;
        // 计算一共有多少个数
        for (int i = 1; i <= length; i++) {
            all += i;
        }
        // 从虚拟的-1层开始算
        int x = -1, y = 0;
        int deep = 0;
        for (int i = 0; i < all; i++) {
            // 判断是不是进入内一层
            if (x == (deep + 1) * 2 - 1 && y == deep + 1) {
                deep++;
            }
            if (y == deep && x < length - 1 - deep) {
                // 判断是不是往下的
                x++;
            } else if (x == length - 1 - deep && y < length - 1 - deep * 2) {
                // 判断是不是往右的
                y++;
            } else if (length - x - deep == length - y - deep * 2) {
                // 判断是不是往左上的
                x--;
                y--;
            }
            // 赋值
            ints[x][y] = i + 1;
        }
        // 打印出来
        for (int[] anInt : ints) {
            for (int i1 : anInt) {
                if (i1 > 0) {
                    System.out.print(String.format("%5d", i1) + "  ");
                }
            }
            System.out.println();
        }
    }

}
