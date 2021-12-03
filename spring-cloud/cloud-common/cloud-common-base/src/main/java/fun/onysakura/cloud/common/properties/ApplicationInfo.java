package fun.onysakura.cloud.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInfo {

    @Value("${custom.machine-id}")
    public Long machineId = 0L;

    @Value("${custom.business-id}")
    public Long businessId = 0L;

    @Value("${spring.application.name}")
    public String applicationName;

}
