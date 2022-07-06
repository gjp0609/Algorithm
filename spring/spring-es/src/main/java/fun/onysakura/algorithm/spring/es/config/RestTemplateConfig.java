package fun.onysakura.algorithm.spring.es.config;

import lombok.SneakyThrows;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
public class RestTemplateConfig extends AbstractElasticsearchConfiguration {
    @Value("${spring.elasticsearch.rest.username}")
    private String user;
    @Value("${spring.elasticsearch.rest.password}")
    private String password;

    @SneakyThrows
    @Override
    public RestHighLevelClient elasticsearchClient() {
        SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(null, (x509Certificates, s) -> true);
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedToLocalhost()
                .usingSsl(sslBuilder.build(), NoopHostnameVerifier.INSTANCE)
                .withBasicAuth(user, password)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}
