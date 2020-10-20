package com.onysakura.algorithm.spring.jpa.id;

import com.onysakura.algorithm.spring.jpa.id.uidGenerator.UidGenerator;
import com.onysakura.algorithm.spring.jpa.id.uidGenerator.exception.UidGenerateException;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Component
public class IdGenerator implements IdentifierGenerator {

    public static UidGenerator uidGenerator;

    @Value("${custom.workerId}")
    private Long workerId;

    @PostConstruct
    public void init() {
        if (workerId == null) {
            workerId = 0L;
        }
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

}
