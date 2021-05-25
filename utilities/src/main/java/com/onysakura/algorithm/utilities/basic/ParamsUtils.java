package com.onysakura.algorithm.utilities.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.onysakura.algorithm.utilities.basic.str.StringUtils;
import com.onysakura.algorithm.utilities.exception.ParamCheckException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@SuppressWarnings("unused")
public class ParamsUtils {

    private static final String EQUAL = "=";
    private static final String DELIMITER = "&";

    /**
     * 将待加密的参数组装为字符串
     *
     * @param map 参数
     */
    public static String join(Map<String, String> map) {
        return join(map, false);
    }

    /***
     * 将待加密的参数组装为字符串
     * @param map 参数
     * @param ignoreBlankValue 是否忽略空值
     */
    public static String join(Map<String, String> map, boolean ignoreBlankValue) {
        return join(map, ignoreBlankValue, EQUAL, DELIMITER);
    }

    /**
     * 将待加密的参数组装为字符串
     *
     * @param map              参数
     * @param ignoreBlankValue 是否忽略空值
     * @param equal            key 和 value 的连接符
     * @param delimiter        参数之间的连接符
     */
    public static String join(Map<String, String> map, boolean ignoreBlankValue, String equal, String delimiter) {
        ArrayList<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (ignoreBlankValue) {
                if (StringUtils.isBlank(value)) {
                    continue;
                }
            }
            String s = key + equal + value;
            list.add(s);
        }
        Collections.sort(list);
        return String.join(delimiter, list);
    }

    /**
     * 除去数组中的空值和签名参数
     *
     * @param params       签名参数对象
     * @param removeEmpty  是否移除空值
     * @param ignoreParams 忽略的 key
     */
    public static Map<String, String> paramFilter(Map<String, String> params, boolean removeEmpty, String... ignoreParams) {
        Map<String, String> result = new HashMap<>();
        if (params == null || params.size() <= 0) {
            return result;
        }
        key:
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (removeEmpty && StringUtils.isBlank(value)) {
                continue;
            }
            for (String ignoreParam : ignoreParams) {
                if (StringUtils.equals(key, ignoreParam)) {
                    continue key;
                }
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 对象转 map
     *
     * @param obj 待转换对象
     */
    public static Map<String, String> toMap(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            Map<String, String> map = new HashMap<>();
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
            for (String key : jsonObject.keySet()) {
                map.put(key, jsonObject.getString(key));
            }
            return map;
        } catch (Exception e) {
            log.error("object to map error", e);
            return null;
        }
    }

    /**
     * 通用参数校验
     */
    public static <Param> Map<String, String> paramsCheckAll(Param param) throws ParamCheckException {
        if (param == null) {
            throw new ParamCheckException("参数不能为空");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Param>> validate = validator.validate(param);
        HashMap<String, String> map = new HashMap<>();
        for (ConstraintViolation<Param> violation : validate) {
            map.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return map.isEmpty() ? null : map;
    }

    /**
     * 通用参数校验
     */
    public static <Param> void paramsCheck(Param param) throws ParamCheckException {
        if (param == null) {
            throw new ParamCheckException("参数不能为空");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Param>> validate = validator.validate(param);
        for (ConstraintViolation<Param> violation : validate) {
            throw new ParamCheckException(violation.getMessage());
        }
    }
}
