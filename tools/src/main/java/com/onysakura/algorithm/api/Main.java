package com.onysakura.algorithm.api;

import com.onysakura.algorithm.utilities.web.httpclient.*;

public class Main {

    private static final String URL = "https://httpbin.org/";

    public static void main(String[] args) throws Exception {
//        get();
//        post();
//        postJson();
//        postXml();
//        postForm();
        postFile();
    }

    public static void get() throws Exception {
        HttpClientUtils.get(URL + "get", new GetParam().addParam("k", "v").addParam("k2", "v2"));
    }

    public static void post() throws Exception {
        HttpClientUtils.post(URL + "post",
                new PostParam()
                        .addParam("k1", "v1")
                        .addParam("k2", "v2")
                        .addHeader("Authorization", "AuthorizationValue")
        );
    }

    public static void postJson() throws Exception {
        HttpClientUtils.post(URL + "post",
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.APPLICATION_JSON)
                        .addBody("k1", "v1")
                        .addBody("k2", "v2")
        );
    }

    public static void postXml() throws Exception {
        ResponseResult responseResult = HttpClientUtils.post(URL + "post",
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.APPLICATION_XML)
                        .addBody("k1", "v1")
                        .addBody("k2", "v2")
        );
        System.out.println(responseResult.getResult());
    }

    public static void postForm() throws Exception {
        HttpClientUtils.post(URL + "post",
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.APPLICATION_FORM_URLENCODED)
                        .addBody("k1", "v1")
                        .addBody("k2", "v2")
        );
    }

    public static void postFile() throws Exception {
        HttpClientUtils.post(URL + "post",
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.MULTIPART_FORM_DATA)
                        .addBody("k1", "v1")
                        .addBody("k2", "v2")
                        .addUploadFilePath("/Files/Temp/Types/text.txt")
                        .addUploadFileName("text")
                        .addUploadFilePath("/Files/Temp/Types/image.jpg")
                        .addUploadFileName("data")
        );
    }
}
