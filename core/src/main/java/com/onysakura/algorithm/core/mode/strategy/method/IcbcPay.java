package com.onysakura.algorithm.core.mode.strategy.method;

import com.onysakura.algorithm.core.mode.strategy.dto.base.BaseInitParam;
import com.onysakura.algorithm.core.mode.strategy.dto.base.BaseResult;
import com.onysakura.algorithm.core.mode.strategy.strategy.PayStrategy;

public class IcbcPay implements PayStrategy {

    @Override
    public BaseResult getPayParams(BaseInitParam initParam) {
        System.out.println(initParam);
        return null;
    }
}
