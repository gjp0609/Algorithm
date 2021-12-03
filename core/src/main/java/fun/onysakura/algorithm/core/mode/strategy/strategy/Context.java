package fun.onysakura.algorithm.core.mode.strategy.strategy;

import fun.onysakura.algorithm.core.mode.strategy.dto.PayConstants;
import fun.onysakura.algorithm.core.mode.strategy.exception.PayException;

import java.lang.reflect.Constructor;

public class Context {

    public static PayStrategy get(PayConstants.PayType payType) {
        try {
            Class<? extends PayStrategy> payClass = payType.getPayClass();
            if (payClass != null) {
                Constructor<? extends PayStrategy> constructor = payClass.getDeclaredConstructor();
                return constructor.newInstance();
            }
        } catch (Exception ignored) {
        }
        throw new PayException("暂不支持此支付方式");
    }
}
