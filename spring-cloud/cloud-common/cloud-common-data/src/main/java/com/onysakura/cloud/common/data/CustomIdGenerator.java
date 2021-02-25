package com.onysakura.cloud.common.data;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.onysakura.cloud.common.properties.ApplicationInfo;
import com.onysakura.cloud.common.utils.uidGenerator.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class CustomIdGenerator implements IdentifierGenerator {

    @Autowired
    private ApplicationInfo applicationInfo;

    private UidGenerator uidGenerator;

    @PostConstruct
    public void uidGeneratorInit() {
        log.debug("uid generator init, machineId: {}, businessId: {}", applicationInfo.machineId, applicationInfo.businessId);
        uidGenerator = UidGenerator.getUidGenerator(applicationInfo.machineId, applicationInfo.businessId);
    }

    @Override
    public Long nextId(Object entity) {
        log.debug("nextId: {}", entity);
        return uidGenerator.getUID();
    }
}
