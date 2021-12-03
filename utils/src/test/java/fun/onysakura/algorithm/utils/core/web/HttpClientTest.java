package fun.onysakura.algorithm.utils.core.web;

import fun.onysakura.algorithm.utils.core.web.httpclient.*;
import org.junit.jupiter.api.Test;

public class HttpClientTest {

    private static final String URL = "https://httpbin.org/";

    @Test
    public void get() throws Exception {
        ResponseResult result = HttpClientUtils.get(URL + "get", new GetParam().addParam("k", "v").addParam("k2", "v2"));
        System.out.println(result.getResult());
    }

    @Test
    public void post() throws Exception {
        ResponseResult result = HttpClientUtils.post(URL + "post",
                new PostParam()
                        .addParam("k1", "v1")
                        .addParam("k2", "v2")
                        .addHeader("Authorization", "AuthorizationValue")
        );
        System.out.println(result.getResult());
    }

    @Test
    public void postJson() throws Exception {
        ResponseResult result = HttpClientUtils.post(URL + "post",
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.APPLICATION_JSON)
                        .addBody("k1", "v1")
                        .addBody("k2", "v2")
        );
        System.out.println(result.getResult());
    }

    @Test
    public void postXml() throws Exception {
        ResponseResult responseResult = HttpClientUtils.post(URL + "post",
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.APPLICATION_XML)
                        .addBody("k1", "v1")
                        .addBody("k2", "v2")
        );
        System.out.println(responseResult.getResult());
    }

    @Test
    public void postForm() throws Exception {
        ResponseResult result = HttpClientUtils.post(URL + "post",
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.APPLICATION_FORM_URLENCODED)
                        .addBody("k1", "v1")
                        .addBody("k2", "v2")
        );
        System.out.println(result.getResult());
    }

    @Test
    public void postFile() throws Exception {
        ResponseResult result = HttpClientUtils.post(URL + "post",
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.MULTIPART_FORM_DATA)
                        .addBody("k1", "v1")
                        .addBody("k2", "v2")
                        .addUploadFilePath("/Files/Temp/Types/text.txt")
                        .addUploadFileName("text")
                        .addUploadFilePath("/Files/Temp/Types/image.jpg")
                        .addUploadFileName("data")
        );
        System.out.println(result.getResult());
    }
}
