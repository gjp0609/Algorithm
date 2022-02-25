package fun.onysakura.algorithm.spring.resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class ResourcesConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 指定匹配路径对应的文件
        registry.addResourceHandler("/files/**").addResourceLocations("classpath:/test/");
        super.addResourceHandlers(registry);
    }
}
