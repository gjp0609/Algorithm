package fun.onysakura.algorithm.utils.core.web;

import fun.onysakura.algorithm.utils.core.web.security.MD5Utils;
import fun.onysakura.algorithm.utils.core.web.security.RsaUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SecurityTest {

    @Test
    public void md5() {
        String src = "String";
        String for32 = MD5Utils.for32(src);
        System.out.println(for32);
        String forCSharp = MD5Utils.forCSharp(src, StandardCharsets.UTF_8, true);
        System.out.println(forCSharp);
    }

    @Test
    public void rsa() {
        String src = "String";
        Charset charset = StandardCharsets.UTF_8;
        Map<String, String> keyPairs = RsaUtils.generateRSAKeyPairs();
        if (keyPairs != null) {
            String publicKey = keyPairs.get("publicKey");
            String privateKey = keyPairs.get("privateKey");
            String sign = RsaUtils.rsaSign(src, privateKey, charset);
            boolean check = RsaUtils.doCheck(src, sign, publicKey, charset);
            System.out.println(check);

            String encrypt = RsaUtils.rsaEncrypt(src, publicKey, charset);
            String decrypt = RsaUtils.rsaDecrypt(encrypt, privateKey, charset);
            System.out.println(src.equals(decrypt));
        }
    }
}
