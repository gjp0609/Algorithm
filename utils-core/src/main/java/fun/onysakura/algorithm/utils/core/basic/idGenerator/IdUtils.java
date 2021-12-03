package fun.onysakura.algorithm.utils.core.basic.idGenerator;

import fun.onysakura.algorithm.utils.core.basic.idGenerator.uidGenerator.UidGenerator;

import java.util.UUID;

@SuppressWarnings("unused")
public class IdUtils {

    private static final SnowflakeIdWorker SNOWFLAKE_ID_WORKER = new SnowflakeIdWorker(0L, 0L);
    private static final UidGenerator UID_GENERATOR = UidGenerator.getUidGenerator(0L);

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static long snowflakeId() {
        return SNOWFLAKE_ID_WORKER.nextId();
    }

    public static long uid() {
        return UID_GENERATOR.getUID();
    }
}
