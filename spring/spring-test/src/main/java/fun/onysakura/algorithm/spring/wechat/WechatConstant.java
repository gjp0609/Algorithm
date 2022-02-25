package fun.onysakura.algorithm.spring.wechat;

import java.util.HashMap;
import java.util.Map;

public class WechatConstant {

    public static Map<String, String> TITLE_MAP = new HashMap<>();
    public static Map<String, String> APP_ID_MAP = new HashMap<>();
    public static Map<String, String> APP_SECRET_MAP = new HashMap<>();
    public static Map<String, String> TOKEN_MAP = new HashMap<>();

    public enum WechatChannel {
        MINE("mine"),
        ;

        private final String code;

        WechatChannel(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    static {
        {
            TITLE_MAP.put(WechatChannel.MINE.code, "测试帐号");
            APP_ID_MAP.put(WechatChannel.MINE.code, "");
            APP_SECRET_MAP.put(WechatChannel.MINE.code, "");
            TOKEN_MAP.put(WechatChannel.MINE.code, "");
        }
    }

    public static final String OPENID_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";

    public static final String AUTH_OPENID_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    public static final String OAUTH2_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURI&response_type=code&scope=snsapi_userinfo&state=info#wechat_redirect";

    public static final String OAUTH2_URL_BASE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURI&response_type=code&scope=snsapi_base&state=base#wechat_redirect";

    public static final String OAUTH2_OPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    public static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public static final String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
}
