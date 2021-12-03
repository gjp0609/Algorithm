package fun.onysakura.algorithm.core.mode.strategy.strategy;

import fun.onysakura.algorithm.core.mode.strategy.dto.base.BaseInitParam;
import fun.onysakura.algorithm.core.mode.strategy.dto.base.BaseResult;

public interface PayStrategy {

    BaseResult getPayParams(BaseInitParam initParam);

}
