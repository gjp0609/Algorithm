package fun.onysakura.algorithm.spring.database.jpa;

import fun.onysakura.algorithm.spring.SingleApplication;
import fun.onysakura.algorithm.spring.jpa.insert.*;
import fun.onysakura.algorithm.utils.core.basic.Benchmark;
import fun.onysakura.algorithm.utils.core.basic.idGenerator.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.UUID;

@SpringBootTest(classes = SingleApplication.class)
@Slf4j
public class TestJpaInsert {

    @Autowired
    private JpaInsertRepositoryUUID uuid;
    @Autowired
    private JpaInsertRepositorySnowflake snowflake;
    @Autowired
    private JpaInsertRepositoryIdentity identity;
    int length = 100_0000;
    int part = 10000;

    enum Type {
        SNOWFLAKE,
        IDENTITY,
        UUID
    }

    @Test
    public void testInsert() {
        for (int i = 0; i < 5; i++) {
            length = 100000;
            this.insertWithUUID();
            this.insertWithSnowflake();
            this.insertWithIdentity();
        }
    }

    @Test
    public void insertWithIdentity() {
        insert(null, Type.IDENTITY, identity);
    }

    @Test
    public void insertWithSnowflake() {
        String[] ids = new String[length];
        SnowflakeIdWorker worker = new SnowflakeIdWorker(0L, 0L);
        for (int i = 0; i < length; i++) {
            ids[i] = String.valueOf(worker.nextId());
        }
        insert(ids, Type.SNOWFLAKE, snowflake);
    }

    @Test
    public void insertWithUUID() {
        String[] ids = new String[length];
        for (int i = 0; i < length; i++) {
            ids[i] = UUID.randomUUID().toString().replace("-", "");
        }
        insert(ids, Type.UUID, uuid);
    }

    private void insert(String[] ids, Type type, JpaRepository repository) {
        Benchmark.init();
        ArrayList<Long> longs = new ArrayList<>();
        for (int i = 0; i < length / part; i++) {
            ArrayList modals = new ArrayList();
            Benchmark.begin();
            for (int j = 0; j < part; j++) {
                switch (type) {
                    case IDENTITY:
                        modals.add(new JpaInsertModalIdentity("test"));
                        break;
                    case SNOWFLAKE:
                        modals.add(new JpaInsertModalSnowflake(ids[i * part + j], "test"));
                        break;
                    case UUID:
                        modals.add(new JpaInsertModalUUID(ids[i * part + j], "test"));
                        break;
                }
            }
            repository.saveAll(modals);
            double v = Benchmark.endWithoutPrint();
            log.info("{} {}, time: {}", type.name(), i, v);
            longs.add((long) v);
        }
        log.info("time: {}", longs);
    }
}
