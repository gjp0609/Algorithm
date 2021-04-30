package com.onysakura.algorithm.utilities.web.httpclient;

import com.alibaba.fastjson.JSON;
import com.onysakura.algorithm.utilities.basic.str.RandomUtils;
import com.onysakura.algorithm.utilities.basic.str.StringUtils;
import com.onysakura.algorithm.utilities.basic.XmlUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings("unused")
public class HttpClientUtils {

    public static ResponseResult get(String httpUrl) throws Exception {
        return get(httpUrl, null);
    }

    public static ResponseResult get(String httpUrl, GetParam getParam) throws Exception {
        log.info("---------- GET: {}", httpUrl);
        log.info("params: {}", JSON.toJSONString(getParam));
        long startTime = System.currentTimeMillis();
        if (StringUtils.isBlank(httpUrl)) {
            throw new RuntimeException("url must not be null");
        }
        if (getParam == null) {
            getParam = new GetParam();
        }
        ResponseResult responseResult = new ResponseResult();
        httpUrl = setUrlParams(httpUrl, getParam);
        log.info("---------- url: {}", httpUrl);
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result;
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(getParam.getConnectTimeout());
            connection.setReadTimeout(getParam.getReadTimeout());
            Map<String, String> headers = getParam.getHeaders();
            if (headers != null) {
                for (String key : headers.keySet()) {
                    connection.setRequestProperty(key, headers.get(key));
                }
            }
            connection.connect();
            responseResult.setResponseCode(connection.getResponseCode());
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            responseResult.setHeaderFields(headerFields);
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                result = br.lines().collect(Collectors.joining("\n"));
                responseResult.setResult(result);
            }
        } finally {
            close(br);
            close(is);
            close(connection);
        }
//        log.debug("Time usage: {}ms, Result: {}", System.currentTimeMillis() - startTime, result);
        return responseResult;
    }

    public static ResponseResult post(String httpUrl) throws Exception {
        return post(httpUrl, null);
    }

    public static ResponseResult post(String httpUrl, PostParam postParam) throws Exception {
        log.info("---------- POST: {}", httpUrl);
        log.info("params: {}", JSON.toJSONString(postParam));
        long startTime = System.currentTimeMillis();
        if (postParam == null) {
            postParam = new PostParam();
        }
        if (StringUtils.isBlank(httpUrl)) {
            throw new RuntimeException("url must not be null");
        }
        httpUrl = setUrlParams(httpUrl, postParam);
        Charset charset = postParam.getCharset();
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        HttpClientConstants.ContentType contentType = postParam.getContentType();
        if (contentType == null) {
            contentType = HttpClientConstants.ContentType.APPLICATION_JSON;
        }
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        DataOutputStream ds = null;
        BufferedReader br = null;
        String body = null;
        ResponseResult responseResult = new ResponseResult();
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(postParam.getConnectTimeout());
            connection.setReadTimeout(postParam.getReadTimeout());
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", contentType.getValueWithCharset(charset.name()));
            Map<String, String> headers = postParam.getHeaders();
            if (headers != null) {
                for (String key : headers.keySet()) {
                    connection.setRequestProperty(key, headers.get(key));
                }
            }
            switch (contentType) {
                case WEBSERVICE_XML_TEXT:
                case APPLICATION_JSON_TEXT:
                    body = postParam.getBody().get("body");
                    break;
                case APPLICATION_XML:
                    body = XmlUtils.mapToXml(postParam.getBody());
                    break;
                case MULTIPART_FORM_DATA:
                    String end = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "----WebKitFormBoundary" + RandomUtils.randomStr(10);
                    connection.setRequestProperty("Content-Type", contentType.getValue() + "; boundary=" + boundary);
                    ds = new DataOutputStream(connection.getOutputStream());
                    Map<String, String> postParamBody = postParam.getBody();
                    if (postParamBody != null) {
                        for (String key : postParamBody.keySet()) {
                            ds.writeBytes(twoHyphens + boundary);
                            ds.writeBytes(end);
                            ds.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"");
                            ds.writeBytes(end);
                            ds.writeBytes("Content-Type: text/plain");
                            ds.writeBytes(end);
                            ds.writeBytes(end);
                            ds.writeBytes(postParamBody.get(key));
                            ds.writeBytes(end);
                        }
                    }
                    List<String> uploadFilePaths = postParam.getUploadFilePaths();
                    List<String> uploadFileNames = postParam.getUploadFileNames();
                    if (uploadFilePaths != null && uploadFilePaths.size() > 0) {
                        for (int i = 0; i < uploadFilePaths.size(); i++) {
                            String uploadFile = uploadFilePaths.get(i);
                            String filename = uploadFile.substring(uploadFile.lastIndexOf("/") + 1);
                            ds.writeBytes(twoHyphens + boundary);
                            ds.writeBytes(end);
                            String uploadFileName = "file" + i;
                            if (uploadFileNames != null) {
                                uploadFileName = uploadFileNames.get(i);
                            }
                            ds.writeBytes("Content-Disposition: form-data; " + "name=\"" + uploadFileName + "\"; filename=\"" + filename + "\"");
                            ds.writeBytes(end);
                            // ds.writeBytes("Content-Type: image/jpeg");
                            // ds.writeBytes(end);
                            ds.writeBytes(end);
                            FileInputStream fis = new FileInputStream(uploadFile);
                            int bufferSize = 1024;
                            byte[] buffer = new byte[bufferSize];
                            int length;
                            while ((length = fis.read(buffer)) != -1) {
                                ds.write(buffer, 0, length);
                            }
                            ds.writeBytes(end);
                            fis.close();
                        }
                        ds.writeBytes(twoHyphens + boundary + twoHyphens);
                        ds.writeBytes(end);
                    }
                    ds.flush();
                    break;
                case APPLICATION_FORM_URLENCODED:
                    StringBuffer postData = new StringBuffer();
                    for (Map.Entry<String, String> param : postParam.getBody().entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), charset.name()));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), charset.name()));
                    }
                    body = postData.toString();
                    break;
                case APPLICATION_JSON:
                default:
                    body = JSON.toJSONString(postParam.getBody());
                    break;
            }
            if (ds == null) {
                os = connection.getOutputStream();
                os.write(body.getBytes());
                os.flush();
            }
            responseResult.setResponseCode(connection.getResponseCode());
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            responseResult.setHeaderFields(headerFields);
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String result = br.lines().collect(Collectors.joining("\n"));
                responseResult.setResult(result);
            }
        } finally {
            close(br);
            close(os);
            close(is);
            close(connection);
        }
        log.info("Time usage: {}ms, Result: {}", System.currentTimeMillis() - startTime, JSON.toJSONString(responseResult));
        return responseResult;
    }

    private static String setUrlParams(String httpUrl, GetParam getParam) throws Exception {
        Map<String, String> params = getParam.getParams();
        if (params != null) {
            ArrayList<String> list = new ArrayList<>();
            for (String key : params.keySet()) {
                String value = params.get(key);
                list.add(key + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8.name()));
            }
            if (httpUrl.contains("?")) {
                httpUrl = httpUrl + "&";
            } else {
                httpUrl = httpUrl + "?";
            }
            httpUrl = httpUrl + String.join("&", list);
        }
        return httpUrl;
    }

    private static void close(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
    }

    private static void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                log.warn("close stream fail", e);
            }
        }
    }
}
