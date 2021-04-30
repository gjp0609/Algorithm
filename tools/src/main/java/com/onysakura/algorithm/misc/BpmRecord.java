package com.onysakura.algorithm.misc;

import java.util.LinkedList;
import java.util.Scanner;

public class BpmRecord {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.println("begin");
        EvictingQueue<Long> queue = new EvictingQueue<>(3);
        long begin1 = System.currentTimeMillis();
        long count = 0L;
        while (true) {
            count++;
            long begin = System.currentTimeMillis();
            String s = scanner.nextLine();
            if ("q".equalsIgnoreCase(s)) {
                return;
            }
            long step = System.currentTimeMillis() - begin;
            long step1 = System.currentTimeMillis() - begin1;
            queue.add(step);
            System.out.printf("\rcurrent bpm: \033[35m%4.0f\033[0m, average bpm: %4.0f", 60D * 1000D / average(queue), 60D * 1000D / (step1 / count));
        }
    }

    public static double average(EvictingQueue<Long> queue) {
        int size = queue.size();
        long sum = 0L;
        for (Long aLong : queue) {
            sum += aLong;
        }
        return 1D * sum / size;
    }
}

class EvictingQueue<E> extends LinkedList<E> {
    private final int limit;

    public EvictingQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) {
            super.remove();
        }
        return true;
    }
}
