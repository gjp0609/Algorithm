package fun.onysakura.cloud.platform.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonValue {

    public static String APPLICATION_NAME;

    @Value("spring.application.name")
    public void setApplicationName(String applicationName) {
        CommonValue.APPLICATION_NAME = applicationName;
    }
}
