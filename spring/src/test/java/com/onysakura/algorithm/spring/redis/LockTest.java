package com.onysakura.algorithm.spring.redis;

import com.onysakura.algorithm.spring.SingleApplication;
import com.onysakura.algorithm.spring.transaction.TransactionTest;
import com.onysakura.algorithm.spring.utils.Benchmark;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = SingleApplication.class)
public class LockTest {

    private final Logger log = LoggerFactory.getLogger(TransactionTest.class);
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_KEY = "LockTestLockKey";
    private static final String NUM_KEY = "LockTestNumKey";

    private static final int INIT_VALUE = 1000;
    private int num;

    @Test
    public void main() throws Exception {
        Benchmark.init();
        {
            num = INIT_VALUE;
            redisTemplate.opsForValue().set(NUM_KEY, String.valueOf(num));
            ExecutorService executorService = Executors.newFixedThreadPool(6);
            Benchmark.begin();
            for (int i = 0; i < 1000; i++) {
                executorService.submit(this::subtractWithLock);
            }
            log.debug("submit finish");
            executorService.shutdown();
            executorService.awaitTermination(60, TimeUnit.SECONDS);
            Benchmark.end();
            // 循环 1000 次实际消耗值
            log.info("num with lock -> {}", INIT_VALUE - num);
        }
        {
            num = INIT_VALUE;
            redisTemplate.opsForValue().set(NUM_KEY, String.valueOf(num));
            ExecutorService executorService = Executors.newFixedThreadPool(6);
            Benchmark.begin();
            for (int i = 0; i < 1000; i++) {
                executorService.submit(this::subtractWithoutLock);
            }
            log.debug("submit finish");
            executorService.shutdown();
            executorService.awaitTermination(60, TimeUnit.SECONDS);
            Benchmark.end();
            log.info("num without lock -> {}", INIT_VALUE - num);
        }
    }

    public void subtractWithLock() {
        lock();
        subtractWithoutLock();
        unLock();
    }

    public void subtractWithoutLock() {
        // num--; // 一样会因为高并发而出现错误，只是运算太快，不易触发
        String s = redisTemplate.opsForValue().get(NUM_KEY);
        if (s != null) {
            num = Integer.parseInt(s) - 1;
            redisTemplate.opsForValue().set(NUM_KEY, String.valueOf(num));
        }
    }

    public void lock() {
        Boolean lock;
        long timeoutInSecond = 10;
        String value = "v";
        lock = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, value, timeoutInSecond, TimeUnit.SECONDS);
        while (lock == null || !lock) {
            try {
                Thread.sleep(300);
                log.debug("blocking");
            } catch (InterruptedException e) {
            }
            lock = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, value, timeoutInSecond, TimeUnit.SECONDS);
        }
    }

    public void unLock() {
        redisTemplate.delete(LOCK_KEY);
    }
}
