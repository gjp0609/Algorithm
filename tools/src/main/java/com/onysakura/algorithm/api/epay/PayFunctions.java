package com.onysakura.algorithm.api.epay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.onysakura.algorithm.utilities.basic.ParamsUtils;
import com.onysakura.algorithm.utilities.web.security.RsaUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PayFunctions {
    public static void main(String[] args) {
        Charset charset = StandardCharsets.UTF_8;
        String pub = "-";
        String pri = "-";
        String pub2 = "-";
        String pri2 = "-";

        String s = "-";
        JSONObject jsonObject = JSON.parseObject(s);
        System.out.println(jsonObject.toString(SerializerFeature.PrettyFormat));
        Map<String, String> hashMap = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            String value = jsonObject.getString(key);
            hashMap.put(key, value);
        }
        hashMap = ParamsUtils.paramFilter(hashMap, true);
        String content = ParamsUtils.join(hashMap);// 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
        System.out.println(content);
        String rsaSign = RsaUtils.rsaSign(content, pri, charset);
        System.out.println(rsaSign);
        String join = ParamsUtils.join(hashMap);
        System.out.println(join);
        boolean sign = RsaUtils.doCheck(content, rsaSign, pub, charset);
        System.out.println(sign);
    }
}
