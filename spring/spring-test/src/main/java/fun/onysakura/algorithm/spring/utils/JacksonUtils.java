package fun.onysakura.algorithm.spring.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JacksonUtils {

    // 定义 JACKSON 对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成 JSON 字符串
     *
     * @param obj 需要转换的对象
     * @return 返回 jsonString
     */
    public static String objectToJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("将对象转换成 JSON 字符串异常", e);
        }
        return null;
    }

    /**
     * 将对象转换成JSON字符串,为空字段不序列化
     *
     * @param obj 需要转换的对象
     * @return 返回 jsonString
     */
    public static String objectToJsonNonNull(Object obj) {
        try {
            MAPPER.setSerializationInclusion(Include.NON_NULL);
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("将对象转换成 JSON 字符串异常", e);
        }
        return null;
    }

    /**
     * 将 JSON 字符串集转化为指定对象
     *
     * @param jsonData 字符串
     * @param beanType 对象中的 object 类型
     * @return 返回指定的 beanType 对象
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            log.error("将 JSON 字符串集转化为指定对象异常", e);
        }
        return null;
    }

    /**
     * 将JSON字符串转换成POJO对象list
     *
     * @param jsonData JSON 字符串
     * @param beanType 对象中的 object 类型
     * @return 返回指定的 beanType list 对象
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            log.error("将 JSON 字符串转换成 POJO 对象 list 异常", e);
        }
        return null;
    }

    /**
     * 判断 JSON 字符串是否数组
     *
     * @param jsonData JSON 字符串
     * @param beanType 对象中的 object 类型
     */
    public static <T> boolean isList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            MAPPER.readValue(jsonData, javaType);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
