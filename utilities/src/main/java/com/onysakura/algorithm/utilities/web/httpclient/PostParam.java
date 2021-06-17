package com.onysakura.algorithm.utilities.web.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class PostParam extends GetParam {
    /**
     * 传输方式
     */
    private HttpClientConstants.ContentType contentType;
    /**
     * 传输编码
     */
    private Charset charset;
    /**
     * 请求内容
     */
    private String body;
    /**
     * 上传文件路径
     */
    private List<String> uploadFileNames;
    /**
     * 上传文件名称
     */
    private List<String> uploadFilePaths;

    public HttpClientConstants.ContentType getContentType() {
        return contentType;
    }

    public PostParam setContentType(HttpClientConstants.ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    public PostParam setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public String getBody() {
        return body;
    }

    public PostParam setBody(String body) {
        this.body = body;
        return this;
    }

    public PostParam setBody(Object body) {
        this.body = JSON.toJSONString(body);
        return this;
    }

    public PostParam addBody(Map<String, String> body) {
        if (this.body == null) {
            this.body = new JSONObject().toJSONString();
        }
        JSONObject jsonObject = JSON.parseObject(this.body);
        jsonObject.putAll(body);

        return this;
    }

    public PostParam addBody(String key, String value) {
        if (this.body == null) {
            this.body = new JSONObject().toJSONString();
        }
        JSONObject jsonObject = JSON.parseObject(this.body);
        jsonObject.put(key, value);
        this.body = jsonObject.toJSONString();
        return this;
    }

    public List<String> getUploadFileNames() {
        return uploadFileNames;
    }

    public PostParam setUploadFileNames(List<String> uploadFileNames) {
        this.uploadFileNames = uploadFileNames;
        return this;
    }

    public PostParam addUploadFileNames(List<String> uploadFileNames) {
        if (uploadFileNames == null) {
            uploadFileNames = new ArrayList<>();
        }
        this.uploadFileNames.addAll(uploadFileNames);
        return this;
    }

    public PostParam addUploadFileName(String uploadFileName) {
        if (uploadFileNames == null) {
            uploadFileNames = new ArrayList<>();
        }
        this.uploadFileNames.add(uploadFileName);
        return this;
    }

    public List<String> getUploadFilePaths() {
        return uploadFilePaths;
    }

    public PostParam setUploadFilePaths(List<String> uploadFilePaths) {
        this.uploadFilePaths = uploadFilePaths;
        return this;
    }

    public PostParam addUploadFilePaths(List<String> uploadFilePaths) {
        if (this.uploadFilePaths == null) {
            this.uploadFilePaths = new ArrayList<>();
        }
        this.uploadFilePaths.addAll(uploadFilePaths);
        return this;
    }

    public PostParam addUploadFilePath(String uploadFilePath) {
        if (this.uploadFilePaths == null) {
            this.uploadFilePaths = new ArrayList<>();
        }
        this.uploadFilePaths.add(uploadFilePath);
        return this;
    }

    @Override
    public PostParam setParams(Map<String, String> params) {
        super.setParams(params);
        return this;
    }

    @Override
    public PostParam addParam(String key, String value) {
        super.addParam(key, value);
        return this;
    }

    @Override
    public PostParam setReadTimeout(int readTimeout) {
        super.setReadTimeout(readTimeout);
        return this;
    }

    @Override
    public PostParam setConnectTimeout(int connectTimeout) {
        super.setConnectTimeout(connectTimeout);
        return this;
    }

    @Override
    public Map<String, String> getHeaders() {
        return super.getHeaders();
    }

    @Override
    public PostParam setHeaders(Map<String, String> headers) {
        super.setHeaders(headers);
        return this;
    }

    @Override
    public PostParam addHeaders(Map<String, String> requestProperty) {
        super.addHeaders(requestProperty);
        return this;
    }

    @Override
    public PostParam addHeader(String key, String value) {
        super.addHeader(key, value);
        return this;
    }
}
