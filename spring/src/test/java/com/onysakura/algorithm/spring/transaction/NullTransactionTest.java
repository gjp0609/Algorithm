package com.onysakura.algorithm.spring.transaction;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootTest
@EnableTransactionManagement
public class NullTransactionTest {

    private final Logger log = LoggerFactory.getLogger(NullTransactionTest.class);

    @Test
    public void main() {
        log.info("asd");
    }
}
