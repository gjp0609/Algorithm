package com.onysakura.algorithm.utilities.web.httpclient;

import java.util.List;
import java.util.Map;

public class ResponseResult {

    private int responseCode;

    private String result;

    private Map<String, List<String>> headerFields;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }

    public void setHeaderFields(Map<String, List<String>> headerFields) {
        this.headerFields = headerFields;
    }

    public boolean isSuccess() {
        return responseCode == 200;
    }


    @Override
    public String toString() {
        return "ResponseResult{" +
                "responseCode=" + responseCode +
                ", result='" + result + '\'' +
                ", headerFields=" + headerFields +
                '}';
    }
}
