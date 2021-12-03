package fun.onysakura.algorithm.utils.basic;

import fun.onysakura.algorithm.utils.core.exception.ParamCheckException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@SuppressWarnings("unused")
public class ParamsUtils extends fun.onysakura.algorithm.utils.core.basic.ParamsUtils {

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
