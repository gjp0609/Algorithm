package fun.onysakura.algorithm.utils.core.basic.str;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
public class RandomUtils {

    /**
     * 随机布尔值
     */
    public static boolean nextBoolean() {
        return org.apache.commons.lang3.RandomUtils.nextBoolean();
    }

    /**
     * 随机数字，从 0 开始 [0,max)
     *
     * @param max 最大范围（不包含）
     */
    public static int nextInt(int max) {
        return nextInt(0, max);
    }

    /**
     * 随机数字 [start,end)
     *
     * @param start 开始（包含）
     * @param end   结束（不包含）
     */
    public static int nextInt(int start, int end) {
        return org.apache.commons.lang3.RandomUtils.nextInt(start, end);
    }

    /**
     * 随机数字字符串
     *
     * @param length 长度
     */
    public static String randomNumber(int length) {
        return randomStr(length, StrType.NUMBER);
    }

    /**
     * 随机数字字符串
     *
     * @param length 长度
     */
    public static String randomStr(int length) {
        return randomStr(length, StrType.NUMBER, StrType.STRING, StrType.STRING_UPPERCASE);
    }

    /**
     * 随机字符串
     *
     * @param length 长度
     */
    public static String randomStrLowercase(int length) {
        return randomStr(length, StrType.NUMBER, StrType.STRING);
    }

    /**
     * 随机字符串
     *
     * @param length   长度
     * @param strTypes 类型
     */
    public static synchronized String randomStr(int length, StrType... strTypes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            StrType str = strTypes[nextInt(strTypes.length)];
            char c = str.content.charAt(nextInt(str.content.length()));
            sb.append(c);
        }
        return sb.toString();
    }

    enum StrType {
        NUMBER("0123456789"),
        STRING("abcdefghijklmnopqrstuvwxyz"),
        STRING_UPPERCASE("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
        ;

        private final String content;

        StrType(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}
