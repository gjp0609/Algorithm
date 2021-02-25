package com.onysakura.algorithm.core.mode.strategy.strategy;

import com.onysakura.algorithm.core.mode.strategy.dto.base.BaseInitParam;
import com.onysakura.algorithm.core.mode.strategy.dto.base.BaseResult;

public interface PayStrategy {

    BaseResult getPayParams(BaseInitParam initParam);

}
