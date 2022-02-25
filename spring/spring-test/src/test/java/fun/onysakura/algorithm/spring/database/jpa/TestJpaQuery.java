package fun.onysakura.algorithm.spring.database.jpa;

import fun.onysakura.algorithm.spring.SingleApplication;
import fun.onysakura.algorithm.spring.jpa.query.JpaQueryRepository;
import fun.onysakura.algorithm.spring.jpa.query.JpaQueryModel;
import fun.onysakura.algorithm.spring.jpa.query.Type;
import fun.onysakura.algorithm.utils.core.basic.Benchmark;
import fun.onysakura.algorithm.utils.core.basic.str.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = SingleApplication.class)
@Slf4j
public class TestJpaQuery {

    @Autowired
    private JpaQueryRepository jpaQueryRepository;

    @Test
    public void init() {
        for (int i = 0; i < 10000; i++) {
            JpaQueryModel model = new JpaQueryModel();
            model.setStr(RandomUtils.randomStr(RandomUtils.nextInt(100) + 10));
            long now = System.currentTimeMillis() / 1000;
            long lastYear = now - 60 * 60 * 24 * 365;
            model.setDate(new Date(1000L * (RandomUtils.nextInt((int) (now - lastYear)) + lastYear)));
            model.setType(RandomUtils.nextBoolean() ? Type.A : Type.B);
            model.setNum(RandomUtils.nextInt(10));
            model.setText(RandomUtils.randomStr(1000));
            jpaQueryRepository.save(model);
        }
    }

    @Test
    public void main() {
        Benchmark.init();
        Benchmark.begin();
        jpaQueryRepository.findAll();
        Benchmark.end();

        for (int i = 1; i < 9; i++) {
            Benchmark.begin();
            List<JpaQueryModel> allByDateLike = jpaQueryRepository.findAllByDateLike("2020-05-0" + i + "%");
            Benchmark.end();
            log.info("count: {}", allByDateLike.size());

            Benchmark.begin();
            List<JpaQueryModel> allByDateLikeUseIndex = jpaQueryRepository.findAllByDateLikeUseIndex("2020-11-0" + i);
            Benchmark.end();
            log.info("count: {}", allByDateLikeUseIndex.size());
        }
    }
}
