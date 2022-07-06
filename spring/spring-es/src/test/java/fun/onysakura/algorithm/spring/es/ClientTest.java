package fun.onysakura.algorithm.spring.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import fun.onysakura.algorithm.utils.core.basic.Benchmark;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class ClientTest {

    @Data
    static class Files {
        private String id;
        private String phone;
        private String name;
        private String path;
        private Integer type;
        private Long size;
        private Date date;
    }

    @Test
    public void client() {
        Benchmark.init();
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "Ca1YfLbS7G+0_R34U+My"));
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "https"))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    try {
                        return httpAsyncClientBuilder
                                .setSSLContext(SSLContexts.custom().loadTrustMaterial(null, (x509Certificates, s) -> true).build())
                                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                .setDefaultCredentialsProvider(credentialsProvider);
                    } catch (Exception e) {
                        log.warn("init error", e);
                    }
                    return null;
                })
                .setRequestConfigCallback(builder -> builder.setConnectTimeout(5000).setSocketTimeout(120000))
                .build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);
        ArrayList<Double> list = new ArrayList<>();
        try {
            for (int i = 0; i < 10; i++) {
                Benchmark.begin();
                SearchResponse<Files> response = client.search(searchRequestBuilder -> searchRequestBuilder
                                .index("user-177")
                                .query(query -> query.term(builder -> builder.field("name").value(v -> v.stringValue("17777777777"))))
                                .query(query -> query.term(builder -> builder.field("path").value(v -> v.stringValue("R:/Files/MavenRepository/repository/org/apache"))))
                                .query(query -> query.matchPhrase(builder -> builder.field("name").query("axis")))
                                .postFilter(filter -> filter.range(range -> range.field("type").gte(JsonData.of("1"))))
                                .sort(sort -> sort.field(builder -> builder.field("date").order(SortOrder.Desc))),
                        Files.class);
                list.add(Benchmark.endWithoutPrint());
//                for (Hit<Files> hit : response.hits().hits()) {
//                    System.out.println(JSON.toJSONString(hit.source(), SerializerFeature.PrettyFormat));
//                }
            }
            System.out.println(list);
        } catch (IOException e) {
            log.warn("query error", e);
        }
    }
}
