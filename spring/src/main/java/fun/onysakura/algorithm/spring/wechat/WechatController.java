package fun.onysakura.algorithm.spring.wechat;

import com.alibaba.fastjson.JSONObject;
import fun.onysakura.algorithm.utils.core.basic.str.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping
public class WechatController {

    @Autowired
    private WechatUtils wechatUtils;

    @GetMapping
    public String check(String signature, String timestamp, String nonce, String echostr) {
        log.info("params: {}, {}, {}, {}", signature, timestamp, nonce, echostr);
        return echostr;
    }

    @GetMapping("/share")
    public HashMap<String, String> get(String url, String wechat) {
        log.info("wechat: {}, url: {}", wechat, url);
        JSONObject tokenAndTicket = wechatUtils.getTokenAndTicket(wechat);
        HashMap<String, String> map = new HashMap<>();
        map.put("appId", WechatConstant.APP_ID_MAP.get(wechat));
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = RandomUtils.randomStr(10);
        String signature = wechatUtils.getSignature(tokenAndTicket.getString("ticket"), timestamp, nonceStr, url);
        map.put("nonceStr", nonceStr);
        map.put("timestamp", timestamp);
        map.put("signature", signature);
        return map;
    }
}
