package com.onysakura.algorithm.utilities.web.security;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@SuppressWarnings("unused")
public class MD5Utils {

    public static String for32(String src) {
        try {
            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            byte[] btInput = src.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.warn("MD5加密失败", e);
            return null;
        }
    }

    /**
     * @param sourceStr  源字符串
     * @param charset    编码 CSharp 默认 UTF_16LE
     * @param isFullByte 是否保留 0 前缀，true: byte.ToString("x2"), false: byte.ToString("x")
     */
    public static String forCSharp(String sourceStr, Charset charset, boolean isFullByte) {
        try {
            // 获得MD5摘要算法的 MessageDigest对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(sourceStr.getBytes(charset));
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            StringBuffer buf = new StringBuffer();
            for (int b : md) {
                int tmp = b;
                if (tmp < 0) {
                    tmp += 256;
                }
                if (tmp < 16 && isFullByte) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(tmp));
            }
            return buf.toString();
        } catch (Exception e) {
            log.warn("MD5加密失败", e);
            return null;
        }
    }
}
