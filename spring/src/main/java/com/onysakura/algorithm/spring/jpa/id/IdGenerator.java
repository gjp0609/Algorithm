package com.onysakura.algorithm.spring.jpa.id;

import com.onysakura.algorithm.utilities.basic.idGenerator.SnowflakeIdWorker;
import com.onysakura.algorithm.utilities.basic.idGenerator.uidGenerator.UidGenerator;
import com.onysakura.algorithm.utilities.basic.idGenerator.uidGenerator.exception.UidGenerateException;
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
    public static SnowflakeIdWorker snowflakeIdWorker;

    @Value("${custom.machine-id}")
    private Long machineId;
    @Value("${custom.business-id}")
    private Long businessId;

    @PostConstruct
    public void init() {
        businessId = businessId == null ? 0L : businessId;
        machineId = machineId == null ? 0L : machineId;
        log.debug("worker id: {}", businessId);
        uidGenerator = UidGenerator.getUidGenerator(machineId, businessId);
        snowflakeIdWorker = new SnowflakeIdWorker(machineId, businessId);
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
