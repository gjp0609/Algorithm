package com.onysakura.algorithm.spring.database.index;

import com.onysakura.algorithm.spring.SingleApplication;
import com.onysakura.algorithm.spring.jpa.id.IdGenerator;
import com.onysakura.algorithm.utilities.basic.Benchmark;
import com.onysakura.algorithm.utilities.basic.str.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = SingleApplication.class)
public class TestIndex {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IdGenerator idGenerator;

    public ArrayList<TestIndexModel> generateData() {
        ArrayList<TestIndexModel> models = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            TestIndexModel model = new TestIndexModel();
            model.setId(idGenerator.getUID());
            model.setStr(RandomUtils.randomStr(20));
            long now = System.currentTimeMillis() / 1000;
            long lastYear = now - 60 * 60 * 24 * 365;
            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(RandomUtils.nextInt((int) (now - lastYear)) + lastYear, 0, ZoneOffset.ofHours(8));
            model.setDate(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            model.setType(RandomUtils.nextBoolean() ? Type.A : Type.B);
            model.setNum(RandomUtils.nextInt(10));
            model.setText(RandomUtils.nextInt(10) > 8 ? RandomUtils.randomStr(2000) : null);
            models.add(model);
        }
        return models;
    }

    @Test
    public void init() {
        jdbcTemplate.execute("drop table if exists `spring_test_index`;");
        jdbcTemplate.execute("" +
                "create table `spring_test_index`\n" +
                "(\n" +
                "    `id`   bigint primary key,\n" +
                "    `str`  varchar(50),\n" +
                "    `date` datetime,\n" +
                "    `type` enum ('A','B'),\n" +
                "    `num`  tinyint(1),\n" +
                "    `text` text\n" +
                ");"
        );
    }

    @Test
    public void saveDataWithSingleUpdate() {
        {
            ArrayList<TestIndexModel> models = generateData();
            ArrayList<String> sqlList = new ArrayList<>();
            for (TestIndexModel model : models) {
                sqlList.add("" +
                        "insert into spring_test_index VALUE (" +
                        "'" + model.getId() + "', " +
                        "'" + model.getStr() + "', " +
                        "'" + model.getDate() + "', " +
                        "'" + model.getType().name() + "', " +
                        "" + model.getNum() + ", " +
                        "'" + model.getText() + "');"
                );
            }
            Benchmark.begin();
            for (String sql : sqlList) {
                jdbcTemplate.update(sql);
            }
            Benchmark.end();
        }
    }

    @Test
    public void saveDataWithFile() throws Exception {
        BufferedWriter allWriter = new BufferedWriter(new FileWriter("R:/Temp/sql/all.sql"));
        ExecutorService service = Executors.newFixedThreadPool(12);
        for (int j = 0; j < 10; j++) {
            int finalJ = j;
            service.submit(() -> {
                ArrayList<TestIndexModel> models = generateData();
                Benchmark.init();
                generateData();
                ArrayList<String> sqlList = new ArrayList<>();
                sqlList.add("insert into spring_test_index VALUES ");
                for (int i = 0; i < models.size(); i++) {
                    TestIndexModel model = models.get(i);
                    String sql = "" +
                            "(" +
                            "'" + model.getId() + "', " +
                            "'" + model.getStr() + "', " +
                            "'" + model.getDate() + "', " +
                            "'" + model.getType().name() + "', " +
                            "" + model.getNum() + ", " +
                            "'" + model.getText() + "')";
                    if (i == models.size() - 1) {
                        sql += ";";
                    } else {
                        sql += ", ";
                    }
                    sqlList.add(sql);
                }
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("R:/Temp/sql/insert" + finalJ + ".sql"));
                    Benchmark.begin();
                    for (String sql : sqlList) {
                        writer.write(sql);
                        writer.newLine();
                    }
                    writer.flush();
                    Benchmark.end();
                    writer.close();
                    allWriter.write("source /mnt/r/Temp/sql/insert" + finalJ + ".sql");
                    allWriter.newLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();
        service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        allWriter.flush();
        allWriter.close();
    }

    @Test
    public void saveDataWithBathUpdate() {
        ArrayList<TestIndexModel> models = generateData();
        String sql = "insert into spring_test_index VALUES (?, ?, ?, ?, ?, ?)";
        ArrayList<Object[]> args = new ArrayList<>();
        for (TestIndexModel model : models) {
            args.add(new Object[]{
                    model.getId(),
                    model.getStr(),
                    model.getDate(),
                    model.getType().name(),
                    model.getNum(),
                    model.getText()
            });
        }
        Benchmark.begin();
        jdbcTemplate.batchUpdate(sql, args);
        Benchmark.end();
    }
}
