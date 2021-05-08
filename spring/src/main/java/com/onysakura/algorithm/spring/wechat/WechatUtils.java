package com.onysakura.algorithm.spring.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.onysakura.algorithm.spring.utils.RedisUtils;
import com.onysakura.algorithm.utilities.web.httpclient.HttpClientUtils;
import com.onysakura.algorithm.utilities.web.httpclient.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
@Component
public class WechatUtils {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private RedisUtils<String> redisUtils;
    private final String WECHAT_ACCESS_TOKEN_AND_TICKET_KEY_PREFIX = "WECHAT_ACCESS_TOKEN_AND_TICKET_";

    @PostConstruct
    public void init() {
        redisUtils = new RedisUtils<>(redisTemplate);
    }

    public JSONObject getTokenAndTicket(String wechat) {
        JSONObject jsonObject;
        String token = redisUtils.getString(WECHAT_ACCESS_TOKEN_AND_TICKET_KEY_PREFIX + wechat);
        if (token != null) {
            jsonObject = JSON.parseObject(token);
        } else {
            jsonObject = new JSONObject();
            String appId = WechatConstant.APP_ID_MAP.get(wechat);
            String appSecret = WechatConstant.APP_SECRET_MAP.get(wechat);
            getTokenFromWx(jsonObject, appId, appSecret);
            getTicketFromWx(jsonObject);
            redisUtils.set(WECHAT_ACCESS_TOKEN_AND_TICKET_KEY_PREFIX + wechat, jsonObject.toJSONString(), 7100);
        }
        log.info(jsonObject.toJSONString());
        return jsonObject;
    }

    private void getTokenFromWx(JSONObject accessTokenTicket, String appId, String appSecret) {
        try {
            log.warn("获取token");
            String requestUrl = WechatConstant.TOKEN_URL.replace("APPID", appId).replace("APPSECRET", appSecret);
            ResponseResult result = HttpClientUtils.get(requestUrl);
            JSONObject jsonObject = JSON.parseObject(result.getResult());
            if (jsonObject != null && jsonObject.containsKey("access_token")) {
                accessTokenTicket.put("createTime", System.currentTimeMillis());
                accessTokenTicket.put("accessToken", jsonObject.getString("access_token"));
            } else {
                log.error("获取token失败: {}", jsonObject);
            }
        } catch (Exception e) {
            log.error("获取token失败", e);
        }
    }

    private void getTicketFromWx(JSONObject accessTokenTicket) {
        try {
            log.warn("获取ticket");
            String requestUrl = WechatConstant.TICKET_URL.replace("ACCESS_TOKEN", accessTokenTicket.getString("accessToken"));
            ResponseResult result = HttpClientUtils.get(requestUrl);
            JSONObject jsonObject = JSON.parseObject(result.getResult());
            if (jsonObject != null) {
                accessTokenTicket.put("ticket", jsonObject.getString("ticket"));
            }
        } catch (Exception e) {
            log.error("获取ticket失败", e);
        }
    }

    public String getSignature(String jsApiTicket, String timestamp, String nonce, String jsUrl) {
        String[] paramArr = new String[]{"jsapi_ticket=" + jsApiTicket, "timestamp=" + timestamp, "noncestr=" + nonce, "url=" + jsUrl};
        Arrays.sort(paramArr);
        String content = paramArr[0].concat("&" + paramArr[1]).concat("&" + paramArr[2]).concat("&" + paramArr[3]);
        log.info("拼接之后的content为:" + content);
        String signature = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.getBytes());
            signature = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            log.warn("签名失败", e);
        }
        return signature;
    }

    private String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (byte b : byteArray) {
            strDigest.append(byteToHexStr(b));
        }
        return strDigest.toString();
    }

    private String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        return new String(tempArr);
    }
}
