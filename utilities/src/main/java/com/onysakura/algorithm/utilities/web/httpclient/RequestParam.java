package com.onysakura.algorithm.utilities.web.httpclient;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class RequestParam {
    /**
     * 读取主机服务器返回数据超时时间，默认：60000 毫秒
     */
    private int readTimeout = 60000;
    /**
     * 连接主机服务器超时时间，默认：15000 毫秒
     */
    private int connectTimeout = 15000;
    /**
     * headers
     */
    private Map<String, String> headers;

    public int getReadTimeout() {
        return readTimeout;
    }

    public RequestParam setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public RequestParam setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public RequestParam setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RequestParam addHeaders(Map<String, String> requestProperty) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.putAll(requestProperty);
        return this;
    }

    public RequestParam addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }


    @Override
    public String toString() {
        return "RequestParam{" +
                "readTimeout=" + readTimeout +
                ", connectTimeout=" + connectTimeout +
                ", headers=" + headers +
                '}';
    }
}
