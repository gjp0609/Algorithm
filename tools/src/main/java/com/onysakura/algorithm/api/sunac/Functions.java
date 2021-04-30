package com.onysakura.algorithm.api.sunac;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.onysakura.algorithm.utilities.basic.ParamsUtils;
import com.onysakura.algorithm.utilities.web.httpclient.GetParam;
import com.onysakura.algorithm.utilities.web.httpclient.HttpClientUtils;
import com.onysakura.algorithm.utilities.web.httpclient.ResponseResult;
import com.onysakura.algorithm.utilities.web.security.MD5Utils;

import java.util.HashMap;

public class Functions {

    private static final String URL = "-";
    //    private static final String URL = "http://127.0.0.1:8080/member/cust/member";
    private static final String CHANNEL_ID = "test123";
    private static final String CHANNEL_SECRET = "cc03e747a6afbbcbf8be7668acfebee5";

    public static void main(String[] args) throws Exception {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        {
            HashMap<String, String> params = new HashMap<>();
//            params.put("mobile", "");
//            params.put("memberId", "");
            params.put("crUpdateTime", "2020-12-31 23:59:59");
            params.put("page", "1");
            params.put("size", "2");
            params.put("timeStamp", timeStamp);
            String sign = getSign(params);
            memberInfo(params, sign);
        }
//        {
//            HashMap<String, String> params = new HashMap<>();
//            params.put("srcYrProjectId", "");
//            params.put("roomId", "");
//            params.put("memberId", "");
//            params.put("crUpdateTime", "2020-12-07 16:07:39");
//            params.put("page", "1");
//            params.put("size", "50");
//            params.put("timeStamp", timeStamp);
//            String sign = getSign(params);
//            userHouseInfo(params, sign);
//        }

    }

    public static void memberInfo(HashMap<String, String> params, String sign) throws Exception {
        String url = URL + "/memberInfo";
        String timeStamp = params.remove("timeStamp");
        ResponseResult resp = HttpClientUtils.get(url,
                new GetParam()
                        .addHeader("channelId", CHANNEL_ID)
                        .addHeader("signature", sign)
                        .addHeader("timeStamp", timeStamp)
                        .addParams(params)
        );
        System.out.println(JSON.parseObject(resp.getResult()).toString(SerializerFeature.PrettyFormat));
    }

    public static void userHouseInfo(HashMap<String, String> params, String sign) throws Exception {
        String url = URL + "/userHouseInfo";
        String timeStamp = params.remove("timeStamp");
        ResponseResult resp = HttpClientUtils.get(url,
                new GetParam()
                        .addHeader("channelId", CHANNEL_ID)
                        .addHeader("signature", sign)
                        .addHeader("timeStamp", timeStamp)
                        .addParams(params)
        );
        System.out.println(resp);
        System.out.println(JSON.parseObject(resp.getResult()).toString(SerializerFeature.PrettyFormat));
    }

    public static String getSign(HashMap<String, String> params) throws Exception {
        String timeStamp = params.remove("timeStamp");
        String signStr = ParamsUtils.join(params);
        params.put("timeStamp", timeStamp);
        signStr += "&app_secret=" + CHANNEL_SECRET + "&timeStamp=" + timeStamp;
        return MD5Utils.for32(signStr);
    }
}
