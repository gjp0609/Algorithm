package com.onysakura.algorithm.spring.jpa.id;

import com.onysakura.algorithm.utilities.uidGenerator.UidGenerator;
import com.onysakura.algorithm.utilities.uidGenerator.exception.UidGenerateException;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Component
public class IdGenerator implements IdentifierGenerator {

    private final Logger log = LoggerFactory.getLogger(IdGenerator.class);

    public static UidGenerator uidGenerator;

    @Value("${custom.worker-id}")
    private Long workerId;

    @PostConstruct
    public void init() {
        if (workerId == null) {
            workerId = 0L;
        }
        log.debug("worker id: {}", workerId);
        uidGenerator = UidGenerator.getUidGenerator(workerId);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        try {
            return uidGenerator.getUID();
        } catch (UidGenerateException ignored) {
        }
        return null;
    }

    public long getUID() {
        return uidGenerator.getUID();
    }

}
