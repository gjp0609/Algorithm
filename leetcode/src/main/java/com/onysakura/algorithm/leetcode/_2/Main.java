package com.onysakura.algorithm.leetcode._2;

import com.onysakura.algorithm.leetcode.Benchmark;

import java.math.BigInteger;
import java.util.Random;

/**
 * 两数相加
 * <a href="https://leetcode-cn.com/problems/add-two-numbers/">link</a>
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * <p>
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Benchmark.init();
        ListNode param1 = new ListNode(2, new ListNode(4, new ListNode(3)));
        ListNode param2 = new ListNode(5, new ListNode(6, new ListNode(4)));
        {
            param1 = new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9)))));
            param2 = new ListNode(9, new ListNode(9, new ListNode(9)));
        }
        {
            Random random = new Random();
            {
                param1 = new ListNode(random.nextInt(9) + 1);
                ListNode temp = param1;
                for (int i = 0; i < 5000; i++) {
                    temp = temp.next = new ListNode(random.nextInt(9) + 1);
                }
            }
            {
                param2 = new ListNode(random.nextInt(9) + 1);
                ListNode temp = param2;
                for (int i = 0; i < 3009; i++) {
                    temp = temp.next = new ListNode(random.nextInt(9) + 1);
                }
            }
        }
        BigInteger p1 = buildNum(param1);
        BigInteger p2 = buildNum(param2);
        ListNode listNode = new ListNode();
        {
            Benchmark.begin();
            listNode = addTwoNumbers(param1, param2);
            Benchmark.end();
            BigInteger num = buildNum(listNode);
            System.out.println(p1 + " + " + p2 + " = " + p1.add(p2) + " == " + num);
            System.out.println(p1.add(p2).equals(num));
        }
        {
            Benchmark.begin();
            listNode = addTwoNumbers1(param1, param2);
            Benchmark.end();
            BigInteger num = buildNum(listNode);
            System.out.println(p1 + " + " + p2 + " = " + p1.add(p2) + " == " + num);
            System.out.println(p1.add(p2).equals(num));
        }
        {
            Benchmark.begin();
            listNode = addTwoNumbers2(param1, param2);
            Benchmark.end();
            BigInteger num = buildNum(listNode);
            System.out.println(p1 + " + " + p2 + " = " + p1.add(p2) + " == " + num);
            System.out.println(p1.add(p2).equals(num));
        }
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode first = new ListNode();
        int i = l1.val + l2.val;
        first.val = i % 10;
        boolean isRoundUp = i > 9;
        ListNode temp = first;
        l1 = l1.next;
        l2 = l2.next;
        while (l1 != null || l2 != null) {
            temp.next = new ListNode();
            temp = temp.next;
            int val = 0;
            if (l1 != null) {
                val += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                val += l2.val;
                l2 = l2.next;
            }
            if (isRoundUp) {
                val++;
                isRoundUp = false;
            }
            if (val >= 10) {
                val = val % 10;
                isRoundUp = true;
            }
            temp.val = val;
        }
        if (isRoundUp) {
            temp.next = new ListNode(1);
        }
        return first;
    }

    /**
     * 由于输入的两个链表都是逆序存储数字的位数的，因此两个链表中同一位置的数字可以直接相加。
     * 我们同时遍历两个链表，逐位计算它们的和，并与当前位置的进位值相加。具体而言，如果当前两个链表处相应位置的数字为 n1,n2n1,n2，进位值为 carry，则它们的和为 n1+n2+carry；
     * 其中，答案链表处相应位置的数字为 (n1+n2+carry)%10，而新的进位值为 (n1+n2+carry)/10
     * 如果两个链表的长度不同，则可以认为长度短的链表的后面有若干个 00 。
     * 此外，如果链表遍历结束后，有 carry>0，还需要在答案链表的后面附加一个节点，节点的值为 carry。
     * <p>
     * 时间复杂度：O(max(m,n))，其中 m,nm,n 为两个链表的长度。我们要遍历两个链表的全部位置，而处理每个位置只需要 O(1) 的时间。
     * 空间复杂度：O(max(m,n))。答案链表的长度最多为较长链表的长度 +1。
     */
    public static ListNode addTwoNumbers1(ListNode l1, ListNode l2) {
        ListNode head = null, tail = null;
        int carry = 0;
        while (l1 != null || l2 != null) {
            int n1 = l1 != null ? l1.val : 0;
            int n2 = l2 != null ? l2.val : 0;
            int sum = n1 + n2 + carry;
            if (head == null) {
                head = tail = new ListNode(sum % 10);
            } else {
                tail.next = new ListNode(sum % 10);
                tail = tail.next;
            }
            carry = sum / 10;
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (carry > 0) {
            tail.next = new ListNode(carry);
        }
        return head;
    }

    /**
     * 递归方式
     */
    public static ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        return dfs(l1, l2, 0);
    }


    private static ListNode dfs(ListNode l, ListNode r, int i) {
        if (l == null && r == null && i == 0) {
            return null;
        }
        int sum = (l != null ? l.val : 0) + (r != null ? r.val : 0) + i;
        var node = new ListNode(sum % 10);
        node.next = dfs(l != null ? l.next : null, r != null ? r.next : null, sum / 10);
        return node;
    }


    public static BigInteger buildNum(ListNode listNode) {
        StringBuilder num = new StringBuilder();
        do {
            num.insert(0, listNode.val);
            listNode = listNode.next;
        } while (listNode != null);
        return new BigInteger(num.toString());
    }
}


class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
