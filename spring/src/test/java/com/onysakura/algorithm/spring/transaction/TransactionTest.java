package com.onysakura.algorithm.spring.transaction;

import com.onysakura.algorithm.spring.jpa.id.uidGenerator.UidGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@SpringBootTest
@EnableTransactionManagement
public class TransactionTest {

    private final Logger log = LoggerFactory.getLogger(TransactionTest.class);

    @Autowired
    private TransactionTestRepository transactionTestRepository;
    @Autowired
    private TransactionTestService transactionTestService;

    private static final UidGenerator uidGenerator = UidGenerator.getUidGenerator(0L);
    private long testNo;

    @BeforeEach
    public void before() {
        testNo = uidGenerator.getUID();
    }

    public void print() {
        log.info("saved data: [{}]",
                transactionTestRepository.findAll(Example.of(new TransactionTestModel(testNo, null)))
                        .stream()
                        .map(TransactionTestModel::getText)
                        .collect(Collectors.joining(", "))
        );

    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void required_required() {
        log.info("\033[35m -------------------- required_required -------------------- \033[0m");
        log.info("外层 required，内层也是 required，内层使用外层事务，内层报错则内层事务外层事务都回滚");
        try {
            transactionTestService.required_required(testNo);
        } catch (UnexpectedRollbackException e) {
            log.info("error");
        }
        print();
        log.info("\033[35m -------------------- required_required -------------------- \033[0m");
    }


    @Test
    public void required_requiresNew() {
        log.info("\033[35m -------------------- required_requiresNew -------------------- \033[0m");
        log.info("外层 required，内层是 requiredNew，内层使用独立事务，内层报错，其他子事务及外层事务都不回滚");
        try {
            transactionTestService.required_requiresNew(testNo);
        } catch (UnexpectedRollbackException e) {
            log.info("error");
        }
        print();
        log.info("\033[35m -------------------- required_requiresNew -------------------- \033[0m");
    }

    @Test
    public void supports_required() {
        log.info("\033[35m -------------------- supports_required -------------------- \033[0m");
        try {
            log.info("外层 supports，内层是 required，内层使用独立事务，内层报错，其他子事务及外层事务都不回滚");
            transactionTestService.supports_required(testNo);
        } catch (UnexpectedRollbackException e) {
            log.info("error");
        }
        print();
        log.info("\033[35m -------------------- supports_required -------------------- \033[0m");
    }
}
