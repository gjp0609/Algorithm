package fun.onysakura.algorithm.utils.core.basic;

import java.util.HashMap;

public class Benchmark {

    private static final ThreadLocal<HashMap<String, Long>> threadLocal = new ThreadLocal<>();
    private static final double NANO_TO_MILLI = 1000000D;

    public static void init() {
        threadLocal.set(new HashMap<>());
        threadLocal.get().put("count", 0L);
    }

    public static void begin() {
        threadLocal.get().put("count", threadLocal.get().get("count") + 1);
        threadLocal.get().put("startTime", System.nanoTime());
    }

    public static void end() {
        threadLocal.get().put("endTime", System.nanoTime());
        long timeUsage = threadLocal.get().get("endTime") - threadLocal.get().get("startTime");
        double timeUsageInDouble = timeUsage / NANO_TO_MILLI;
        System.out.println("\033[35m " + threadLocal.get().get("count") + ". time usage: " + timeUsageInDouble + "ms\033[0m");
    }

    public static double endWithoutPrint() {
        threadLocal.get().put("endTime", System.nanoTime());
        long timeUsage = threadLocal.get().get("endTime") - threadLocal.get().get("startTime");
        return timeUsage / NANO_TO_MILLI;
    }
}