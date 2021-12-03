package fun.onysakura.algorithm.utils.core.basic;

import org.junit.jupiter.api.Test;

public class BenchmarkTest {

    @Test
    public void test() throws InterruptedException {
        Benchmark.init();
        Benchmark.begin();
        Thread.sleep(1000);
        Benchmark.end();
    }
}
