package fun.onysakura.algorithm.core.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RejectedExecutionHandler {
    static int poolSize = 5;

    public static void main(String[] args) throws Exception {
        {
            AtomicInteger integer = new AtomicInteger(0);
            AtomicInteger errors = new AtomicInteger(0);
            // 有上限的队列
            ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(5);
            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                    poolSize,
                    poolSize * 2,
                    1,
                    TimeUnit.MILLISECONDS,
                    queue,
                    // 当线程数超过队列上限时会触发
                    (r, executor1) -> errors.addAndGet(1)
            );
            for (int i = 0; i < 1000; i++) {
                executor.execute(() -> integer.addAndGet(1));
            }
            executor.shutdown();
//           while (!executor.isTerminated()) {}
            executor.awaitTermination(100, TimeUnit.SECONDS);
            System.out.println("ThreadPoolExecutor 失败数：" + errors.get());
            System.out.println("ThreadPoolExecutor 成功数：" + integer.get());
        }
        {
            AtomicInteger integer = new AtomicInteger(0);
            AtomicInteger errors = new AtomicInteger(0);
            LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                    poolSize,
                    poolSize * 2,
                    1,
                    TimeUnit.MILLISECONDS,
                    queue,
                    (r, executor1) -> errors.addAndGet(1)
            );
            for (int i = 0; i < 1000; i++) {
                // 当线程池停止时继续 execute 会触发 RejectedExecutionHandler
                executor.execute(() -> integer.addAndGet(1));
                if (i > 500) {
                    executor.shutdown();
                }
            }
            executor.shutdown();
            executor.awaitTermination(100, TimeUnit.SECONDS);
            System.out.println("ThreadPoolExecutor 失败数：" + errors.get());
            System.out.println("ThreadPoolExecutor 成功数：" + integer.get());
        }
        {
            AtomicInteger integer2 = new AtomicInteger(0);
            ExecutorService executor = Executors.newFixedThreadPool(poolSize);
            for (int i = 0; i < 1000; i++) {
                executor.execute(() -> integer2.addAndGet(1));
            }
            executor.shutdown();
            executor.awaitTermination(100, TimeUnit.SECONDS);
            System.out.println("Executors 成功数：" + integer2.get());
        }
    }
}
