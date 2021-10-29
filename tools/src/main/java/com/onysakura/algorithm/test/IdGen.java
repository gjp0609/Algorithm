package com.onysakura.algorithm.test;

import com.onysakura.algorithm.utilities.basic.Benchmark;
import com.onysakura.algorithm.utilities.basic.idGenerator.SnowflakeIdWorker;

import java.util.UUID;

public class IdGen {

    public static void main(String[] args) throws Exception {
        int count = 1_000_000;
        Benchmark.init();
        {
            SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0L, 0L);
            Benchmark.begin();
            for (int i = 0; i < count; i++) {
                snowflakeIdWorker.nextId();
            }
            double time = Benchmark.endWithoutPrint();
            System.out.println(time);
        }
        {
            Benchmark.begin();
            for (int i = 0; i < count; i++) {
                UUID.randomUUID();
            }
            double time = Benchmark.endWithoutPrint();
            System.out.println(time);
        }
    }
}
