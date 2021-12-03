package fun.onysakura.algorithm.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;

public class QuickTest {

    public static void main(String[] args) {
        HashMap<String, String> params = new HashMap<>();
        for (String arg : args) {
            System.out.println(arg);
            String[] split = arg.split("=");
            String key = split[0];
            if (key != null && key.startsWith("-")) {
                key = key.substring(1);
                String value = split[1];
                params.put(key, value);
            }
        }
        int i = LocalDateTime.now().get(ChronoField.DAY_OF_MONTH);
        String day = String.format("%06d", i);
        System.out.println(day);
    }
}
