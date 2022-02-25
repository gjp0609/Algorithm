package fun.onysakura.algorithm.spring.threadpool;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

    @Test
    public void main() throws Exception {
        System.out.println();
        HttpClient client = HttpClient.newHttpClient();
        ExecutorService pool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            pool.submit(() -> {
                try {
                    long now = System.currentTimeMillis();
                    String url = "http://pay.onysakura.com/test/test/testThread?time=" + now;
//                    String url = "https://tools.onysakura.com/test/testThread?time=" + now;
//                    String url = "http://wsl/test/test/testThread?time=" + now;
                    HttpRequest httpRequest = HttpRequest
                            .newBuilder(URI.create(url))
                            .GET()
                            .timeout(Duration.ofSeconds(100))
                            .build();
                    HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                    long l = System.currentTimeMillis() - Long.parseLong(response.body());
                    System.out.println(String.format("%3d", l) + "   " + (System.currentTimeMillis() - now));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("end");
    }
}
