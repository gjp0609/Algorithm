package com.onysakura.cloud.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class UserApplication {

    Logger logger = LoggerFactory.getLogger(UserApplication.class);

    @Value("${a}")
    private String a;

    @PostConstruct
    public void main() {
        logger.info("init: {}", a);
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
