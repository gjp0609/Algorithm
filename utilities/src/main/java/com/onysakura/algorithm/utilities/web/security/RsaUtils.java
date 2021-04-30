package com.onysakura.algorithm.utilities.web.security;

import com.onysakura.algorithm.utilities.basic.str.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SuppressWarnings({"unused", "SameParameterValue"})
public class RsaUtils {

    /**
     * RSA 最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA 最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final String RSA = "RSA";

    /**
     * 生成 RSA 公、私钥对
     *
     * @return 公私钥对 map
     */
    public static Map<String, String> generateRSAKeyPairs() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, String> keyMap = new HashMap<>(2);
            keyMap.put("publicKey", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            keyMap.put("privateKey", Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            return keyMap;
        } catch (Exception e) {
            log.warn("生成公私钥对失败", e);
            return null;
        }
    }

    /**
     * RSA2 签名
     *
     * @param content    待签名的字符串
     * @param privateKey RSA 私钥字符串
     * @param charset    字符集编码
     * @return 签名结果
     */
    public static String rsaSign(String content, String privateKey, Charset charset) {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(RSA, charset, new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(priKey);
            if (charset == null) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            byte[] signed = signature.sign();
            return new String(Base64.getEncoder().encode(signed));
        } catch (Exception e) {
            log.error("RSA签名异常", e);
            return null;
        }
    }

    /**
     * RSA2验签
     *
     * @param content   被签名的内容
     * @param sign      签名后的结果
     * @param publicKey RSA公钥
     * @param charset   字符集编码
     * @return 验签结果
     */
    public static boolean doCheck(String content, String sign, String publicKey, Charset charset) {
        try {
            PublicKey pubKey = getPublicKeyFromX509(RSA, new ByteArrayInputStream(publicKey.getBytes()));
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(pubKey);
            signature.update(getContentBytes(content, charset));
            return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
        } catch (Exception e) {
            log.error("RSA验签异常", e);
            return false;
        }
    }

    /**
     * RSA公钥加密
     *
     * @param content   待加密的内容
     * @param publicKey 公钥
     * @param charset   字符集编码
     * @return 密文
     */
    public static String rsaEncrypt(String content, String publicKey, Charset charset) {
        ByteArrayOutputStream out = null;
        try {
            PublicKey pubKey = getPublicKeyFromX509(RSA, new ByteArrayInputStream(publicKey.getBytes()));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            int inputLen = content.getBytes().length;
            out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offset > 0) {
                byte[] bytes;
                if (charset == null) {
                    bytes = content.getBytes();
                } else {
                    bytes = content.getBytes(charset);
                }
                cache = cipher.doFinal(bytes, offset, Math.min(inputLen - offset, MAX_ENCRYPT_BLOCK));
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            log.error("RSA公钥加密异常", e);
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("RSA公钥加密IOException异常", e);
                }
            }
        }
    }

    /**
     * RSA私钥解密
     *
     * @param content    加密字符串
     * @param privateKey 私钥
     * @param charset    字符集编码
     * @return 明文
     */
    public static String rsaDecrypt(String content, String privateKey, Charset charset) {
        ByteArrayOutputStream out = null;
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(RSA, charset, new ByteArrayInputStream(privateKey.getBytes()));
            // RSA解密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] dataBytes = Base64.getDecoder().decode(content);
            int inputLen = dataBytes.length;
            out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_DECRYPT_BLOCK;
            }
            // 解密后的内容
            if (charset == null) {
                return out.toString();
            } else {
                return out.toString(charset);
            }
        } catch (Exception e) {
            log.error("RSA私钥解密异常", e);
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("RSA私钥解密IOException异常", e);
                }
            }
        }
    }

    /**
     * 获取私钥对象
     *
     * @param algorithm 签名方式
     * @param ins       私钥流
     */
    private static PrivateKey getPrivateKeyFromPKCS8(String algorithm, Charset charset, InputStream ins) throws Exception {
        if (ins == null || StringUtils.isEmpty(algorithm)) {
            return null;
        }
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey = readText(ins, charset, true).getBytes();
        encodedKey = Base64.getDecoder().decode(encodedKey);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    /**
     * 获取公钥对象
     *
     * @param algorithm 签名方式
     * @param ins       公钥流
     */
    private static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            StringWriter writer = new StringWriter();
            io(new InputStreamReader(ins), writer, true, true);
            byte[] encodedKey = writer.toString().getBytes();
            // 先base64解码
            encodedKey = Base64.getDecoder().decode(encodedKey);
            return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        } catch (InvalidKeySpecException e) {
            log.error("获取公钥对象InvalidKeySpecException异常", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("获取公钥对象NoSuchAlgorithmException异常", e);
        }
        return null;
    }

    /**
     * 获取字符串对应编码的字节
     *
     * @param content 字符串内容
     * @param charset 字符集编码
     */
    private static byte[] getContentBytes(String content, Charset charset) {
        if (charset == null) {
            return content.getBytes();
        }
        return content.getBytes(charset);
    }

    /**
     * 将指定输入流的所有文本全部读出到一个字符串中
     *
     * @param in      输入流
     * @param charset 字符集编码
     * @param closeIn 是否关闭流
     */
    private static String readText(InputStream in, Charset charset, boolean closeIn) {
        Reader reader = charset == null ? new InputStreamReader(in) : new InputStreamReader(in, charset);
        return readText(reader, closeIn);
    }

    /**
     * 将指定 Reader 的所有文本全部读出到一个字符串中
     *
     * @param in      输入流
     * @param closeIn 是否关闭流
     */
    private static String readText(Reader in, boolean closeIn) {
        StringWriter out = new StringWriter();
        io(in, out, closeIn, true);
        return out.toString();
    }

    /**
     * 从输入流读取内容，写入到输出流中
     *
     * @param in       输入流
     * @param out      输出流
     * @param closeIn  是否关闭流
     * @param closeOut 是否关闭流
     */
    private static void io(Reader in, Writer out, boolean closeIn, boolean closeOut) {
        int bufferSize = DEFAULT_BUFFER_SIZE >> 1;
        char[] buffer = new char[bufferSize];
        int amount;
        try {
            while ((amount = in.read(buffer)) >= 0) {
                out.write(buffer, 0, amount);
            }
            out.flush();
        } catch (Exception e) {
            log.error("从输入流读取内容，写入到输出流中异常", e);
        } finally {
            if (closeIn) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("从输入流读取内容，写入到输出流中异常", e);
                }
            }
            if (closeOut) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("从输入流读取内容，写入到输出流中异常", e);
                }
            }
        }
    }
}
