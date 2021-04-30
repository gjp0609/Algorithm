package com.onysakura.algorithm.utilities.basic.idGenerator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * <pre>{@code
 * +-----------------|-----------+------------+------------+
 * |  delta seconds  |  machine  |  business  |  sequence  |
 * +-----------------|-----------+------------+------------+
 * |     20bits+     |    5bit   |    7bits   |   12bits   |
 * +-----------------|-----------+------------+------------+
 * </pre>
 */
public class SnowflakeIdWorker {

    /**
     * 开始时间戳
     */
    private final long epochSeconds = 1619084985414L;

    /**
     * 机器 id 所占的位数
     */
    private final long machineIdBits = 5L;

    /**
     * 业务 id 所占的位数
     */
    private final long businessIdBits = 7L;

    /**
     * 支持的最大机器 id，结果是 31
     */
    private final long maxMachineId = ~(-1L << machineIdBits);

    /**
     * 支持的最大业务 id，结果是 127
     */
    private final long maxBusinessId = ~(-1L << businessIdBits);

    /**
     * 序列在 id 中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 生成序列的掩码，这里为 4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = ~(-1L << sequenceBits);

    /**
     * 机器 ID 向左移 12 位
     */
    private final long machineIdShift = sequenceBits;

    /**
     * 业务 id 向左移 19 位 (7 + 12)
     */
    private final long businessIdShift = sequenceBits + machineIdBits;

    /**
     * 时间截向左移 24 位 (5 + 7 + 12)
     */
    private final long timestampLeftShift = sequenceBits + machineIdBits + businessIdBits;

    /**
     * 工作机器 ID (0 ~ 31)
     */
    private final long machineId;

    /**
     * 业务 ID (0 ~ 127)
     */
    private final long businessId;

    /**
     * 毫秒内序列 (0 ~ 4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * 最大支持 4096 个实例，共 12 位，取 5 位作机器 id，7 位作业务id
     *
     * @param machineId  机器 id: 0 ~ 31
     * @param businessId 业务 id: 0 ~ 127
     */
    public SnowflakeIdWorker(long machineId, long businessId) {
        if (machineId > maxMachineId || machineId < 0) {
            throw new IllegalArgumentException(String.format("machine Id can't be greater than %d or less than 0", maxMachineId));
        }
        if (businessId > maxBusinessId || businessId < 0) {
            throw new IllegalArgumentException(String.format("business Id can't be greater than %d or less than 0", maxBusinessId));
        }
        this.machineId = machineId;
        this.businessId = businessId;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - epochSeconds) << timestampLeftShift) //
                | (businessId << businessIdShift) //
                | (machineId << machineIdShift) //
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 1);
        long l = snowflakeIdWorker.nextId();
        System.out.println(Long.toBinaryString(l));
    }
}
