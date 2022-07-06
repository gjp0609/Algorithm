package fun.onysakura.algorithm.spring.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
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
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@SpringBootApplication
public class ESApplication {

    @Autowired
    private ElasticsearchRestTemplate template;

    public static void main(String[] args) {
        SpringApplication.run(ESApplication.class, args);
    }

    @PostConstruct
    public void test() throws Exception {
        realRest();
        System.out.println(111);
//        if (true) return;
        Benchmark.init();
//        client();
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("phone", "17777777777"))
                        .must(QueryBuilders.termQuery("path", "R:/Files/MavenRepository/repository/org/apache"))
                        .must(QueryBuilders.matchPhraseQuery("name", "axis"))
                        .filter(QueryBuilders.rangeQuery("type").gte(1)))
                .withPageable(PageRequest.of(0, 10))
                .withSort(SortBuilders.fieldSort("date").order(SortOrder.DESC))
                .build();
        log.info("query: {}", nativeSearchQuery.getQuery());
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Benchmark.begin();
            SearchHits<Files> hits = template.search(nativeSearchQuery, Files.class, IndexCoordinates.of("user-177"));
            list.add(Benchmark.endWithoutPrint());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
//        for (SearchHit<Files> hit : hits) {
//            System.out.println(JSON.toJSONString(hit.getContent(), SerializerFeature.PrettyFormat));
//        }
        }
        System.out.println(list);

        client();
    }

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

    public void client() {
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
        try {
            for (int i = 0; i < 100; i++) {
                SearchResponse<Files> response = client.search(searchRequestBuilder -> searchRequestBuilder
                                .index("user-177")
                                .query(query -> query.term(builder -> builder.field("name").value(v -> v.stringValue("17777777777"))))
                                .query(query -> query.term(builder -> builder.field("path").value(v -> v.stringValue("R:/Files/MavenRepository/repository/org/apache"))))
                                .query(query -> query.matchPhrase(builder -> builder.field("name").query("axis")))
                                .postFilter(filter -> filter.range(range -> range.field("type").gte(JsonData.of("1"))))
                                .sort(sort -> sort.field(builder -> builder.field("date").order(co.elastic.clients.elasticsearch._types.SortOrder.Desc))),
                        Files.class);
                for (Hit<Files> hit : response.hits().hits()) {
                    System.out.println(JSON.toJSONString(hit.source(), SerializerFeature.PrettyFormat));
                }
            }
        } catch (IOException e) {
            log.warn("query error", e);
        }
    }

    public static void realRest() throws Exception {
        HttpsURLConnection.setDefaultHostnameVerifier(NoopHostnameVerifier.INSTANCE);
        SSLSocketFactory socketFactory = SSLContexts.custom().loadTrustMaterial(null, (x509Certificates, s) -> true).build().getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
        Benchmark.init();
        ArrayList<Double> list = new ArrayList<>();
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create().register("https", new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build())).build());
        manager.setMaxTotal(100);
        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setSSLContext(SSLContexts.custom().loadTrustMaterial(null, (x509Certificates, s) -> true).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLSocketFactory(new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build()))
                .setConnectionManager(manager)
                .build();
        HttpPost httpPost = new HttpPost();
        httpPost.setURI(URI.create("https://localhost:9200/user-177/_search"));
        httpPost.addHeader("Authorization", "Basic ZWxhc3RpYzpDYTFZZkxiUzdHKzBfUjM0VStNeQ==");
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContentType("application/json");
        ByteArrayInputStream inputStream = new ByteArrayInputStream("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"phone\":\"17777777777\"}},{\"term\":{\"path\":\"R:/Files/MavenRepository/repository/org/apache\"}},{\"match_phrase\":{\"name\":\"axis\"}}],\"filter\":[{\"range\":{\"type\":{\"gt\":0}}}]}},\"from\":0,\"size\":10,\"sort\":[{\"date\":\"desc\"}]}".getBytes(StandardCharsets.UTF_8));
        entity.setContent(inputStream);
        httpPost.setEntity(entity);
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            Benchmark.begin();
            CloseableHttpResponse response = client.execute(httpPost);
            ;
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(responseString);
            response.close();
            list.add(Benchmark.endWithoutPrint());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        System.out.println(list);
    }
}
