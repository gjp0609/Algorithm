package fun.onysakura.algorithm.core.mode.strategy.dto;

import fun.onysakura.algorithm.core.mode.strategy.method.CmbPay;
import fun.onysakura.algorithm.core.mode.strategy.strategy.PayStrategy;

public interface PayConstants {

    enum PayType {
        ICBC(1, null),
        CMB(2, CmbPay.class),
        CCB(4, null);

        private final int code;
        private final Class<? extends PayStrategy> payClass;

        PayType(int code, Class<? extends PayStrategy> payClass) {
            this.code = code;
            this.payClass = payClass;
        }

        public int getCode() {
            return code;
        }

        public Class<? extends PayStrategy> getPayClass() {
            return payClass;
        }
    }
}
