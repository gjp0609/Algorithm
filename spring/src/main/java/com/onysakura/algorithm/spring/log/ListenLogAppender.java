package com.onysakura.algorithm.spring.log;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedTransferQueue;

/**
 * 监听日志
 */
@Component
public class ListenLogAppender<E> extends UnsynchronizedAppenderBase<E> {

    private final LinkedTransferQueue<LoggingEvent> queue = new LinkedTransferQueue<>();

    @Override
    protected void append(E eventObject) {
        if (eventObject instanceof LoggingEvent) {
            LoggingEvent event = (LoggingEvent) eventObject;
            queue.put(event);
        } else {
            // debug
            System.err.println(eventObject.getClass());
        }
    }

//    @PostConstruct
    public void init() {
        new Thread(() -> {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("/Files/Temp/test.log"));
                for (; ; ) {
                    LoggingEvent event = null;
                    try {
                        event = queue.take();
                    } catch (InterruptedException ignored) {
                    }
                    if (event != null) {
                        writer.write(event.getMessage());
                        writer.newLine();
                        writer.flush();
//                    System.out.println(event.getMessage());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
