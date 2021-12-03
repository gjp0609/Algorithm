package fun.onysakura.algorithm.utils.core.web.httpclient;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class GetParam extends RequestParam {

    /**
     * 请求内容
     */
    private Map<String, String> params;

    public Map<String, String> getParams() {
        return params;
    }

    public GetParam setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public GetParam addParams(Map<String, String> params) {
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        this.params.putAll(params);
        return this;
    }

    public GetParam addParam(String key, String value) {
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        this.params.put(key, value);
        return this;
    }

    @Override
    public GetParam setReadTimeout(int readTimeout) {
        super.setReadTimeout(readTimeout);
        return this;
    }

    @Override
    public GetParam setConnectTimeout(int connectTimeout) {
        super.setConnectTimeout(connectTimeout);
        return this;
    }

    @Override
    public Map<String, String> getHeaders() {
        return super.getHeaders();
    }

    @Override
    public GetParam setHeaders(Map<String, String> headers) {
        super.setHeaders(headers);
        return this;
    }

    @Override
    public GetParam addHeaders(Map<String, String> requestProperty) {
        super.addHeaders(requestProperty);
        return this;
    }

    @Override
    public GetParam addHeader(String key, String value) {
        super.addHeader(key, value);
        return this;
    }


    @Override
    public String toString() {
        return "GetParam{" +
                "params=" + params +
                "} " + super.toString();
    }
}
