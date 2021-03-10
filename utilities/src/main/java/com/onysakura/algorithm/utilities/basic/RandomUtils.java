package com.onysakura.algorithm.utilities.basic;

import java.util.Random;

public class RandomUtils {
    private static final Random RANDOM = new Random();

    public static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }

    public static String randomNumber(int count) {
        return randomStr(count, StrType.NUMBER);
    }

    public static int randomInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static String randomStr(int count) {
        return randomStr(count, StrType.NUMBER, StrType.STRING, StrType.STRING_UPPERCASE);
    }

    public static String randomStrLowercase(int count) {
        return randomStr(count, StrType.NUMBER, StrType.STRING);
    }

    public static synchronized String randomStr(int count, String... strType) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String str = strType[RANDOM.nextInt(strType.length)];
            char c = str.charAt(RANDOM.nextInt(str.length()));
            sb.append(c);
        }
        return sb.toString();
    }

    interface StrType {
        String NUMBER = "0123456789";
        String STRING = "abcdefghijklmnopqrstuvwxyz";
        String STRING_UPPERCASE = STRING.toUpperCase();
    }

}
