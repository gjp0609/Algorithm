package com.onysakura.algorithm.utils.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ParamUtils {
    public static Map<String, String> getParamsFromCommand(String[] args) {
        Map<String, String> params = new HashMap<>();
        try {
            for (String arg : args) {
                log.debug(arg);
                String[] split = arg.split("=");
                String key = split[0];
                if (key != null && key.startsWith("-")) {
                    key = key.substring(1);
                    String value = split[1];
                    params.put(key, value);
                }
            }
        } catch (Exception e) {
            log.warn("parsing params error", e);
        }
        return params;
    }
}
