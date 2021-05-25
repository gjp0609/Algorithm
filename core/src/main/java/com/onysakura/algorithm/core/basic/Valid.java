package com.onysakura.algorithm.core.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.onysakura.algorithm.utilities.basic.ParamsUtils;
import com.onysakura.algorithm.utilities.exception.ParamCheckException;
import jakarta.validation.constraints.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
public class Valid {

    public static void main(String[] args) {
        {
            TestClass testClass = new TestClass();
            testClass.s = "1234";
            testClass.l = 1234L;
            testClass.b = new BigDecimal("1234.1234");
            testClass.b2 = new BigDecimal("1234");
            testClass.reg = "123asdASD";
            testClass.blank = "aaa";
            testClass.max = 5;
            testClass.min = 10;
            try {
                Map<String, String> map = ParamsUtils.paramsCheckAll(testClass);
                System.out.println(map);
            } catch (ParamCheckException e) {
                log.info("check result: {}", e.getMessage());
            }
        }
        {
            TestClass testClass = new TestClass();
            testClass.s = "123456";
            testClass.l = 123456L;
            testClass.b = new BigDecimal("123456.123456");
            testClass.b2 = new BigDecimal("123456.123456");
            testClass.reg = "123asdASD ?-";
            testClass.blank = "     ";
            testClass.max = 20;
            testClass.min = 5;
            try {
                Map<String, String> map = ParamsUtils.paramsCheckAll(testClass);
                log.info("result: \n{}", JSON.toJSONString(map, SerializerFeature.PrettyFormat));
            } catch (ParamCheckException e) {
                log.info("check result: {}", e.getMessage());
            }
        }
        log.info("check done");
    }

    static class TestClass {
        @Digits(integer = 4, message = "too long", fraction = 0)
        private String s;
        @Digits(integer = 4, message = "too long", fraction = 0)
        private Long l;
        @Digits(integer = 4, message = "too long", fraction = 4)
        private BigDecimal b;
        @Digits(integer = 4, message = "too long", fraction = 0)
        private BigDecimal b2;
        @Pattern(regexp = "[0-9a-zA-Z]+", message = "not match")
        private String reg;
        @NotBlank(message = "blank")
        private String blank;
        @Length
        @Max(value = 10, message = "too big")
        private Integer max;
        @Min(value = 10, message = "too small")
        private Integer min;
    }


}
