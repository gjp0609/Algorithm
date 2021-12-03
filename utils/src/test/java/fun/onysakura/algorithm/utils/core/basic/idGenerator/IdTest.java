package fun.onysakura.algorithm.utils.core.basic.idGenerator;

import fun.onysakura.algorithm.utils.core.basic.idGenerator.uidGenerator.UidGenerator;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IdTest {

    @Test
    public void main() throws Exception {
        UidGenerator generator = UidGenerator.getUidGenerator(1L);
        ExecutorService pool = Executors.newFixedThreadPool(20);
        ConcurrentHashMap<Long, Object> map = new ConcurrentHashMap<>();
        Object o = new Object();
        for (int i = 0; i < 1000000; i++) {
            pool.submit(() -> {
                map.put(generator.getUID(), o);
            });
        }
        pool.shutdown();
        pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        System.out.println(map.size());
    }
}
