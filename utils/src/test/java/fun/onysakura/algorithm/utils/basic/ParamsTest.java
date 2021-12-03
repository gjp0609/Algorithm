package fun.onysakura.algorithm.utils.basic;

import fun.onysakura.algorithm.utils.core.exception.ParamCheckException;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Test;

@Slf4j
public class ParamsTest {

    @Test
    public void test() {
        TestModal testModal = new TestModal();
        testModal.setName("asd");
        testModal.setAge(17);
        try {
            ParamsUtils.paramsCheck(testModal);
        } catch (ParamCheckException e) {
            log.warn("check error", e);
        }
        log.info("done");
    }

    @Data
    static class TestModal {
        @NotBlank
        private String name;
        @Range(max = 100, min = 18)
        private Integer age;
    }
}
