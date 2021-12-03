package fun.onysakura.algorithm.core.mode.strategy.method;

import fun.onysakura.algorithm.core.mode.strategy.dto.base.BaseInitParam;
import fun.onysakura.algorithm.core.mode.strategy.dto.base.BaseResult;
import fun.onysakura.algorithm.core.mode.strategy.dto.base.ErrorCode;
import fun.onysakura.algorithm.core.mode.strategy.dto.cmb.CmbResult;
import fun.onysakura.algorithm.core.mode.strategy.strategy.PayStrategy;

public class CmbPay implements PayStrategy {
    @Override
    public BaseResult getPayParams(BaseInitParam initParam) {
        System.out.println(initParam);
        BaseResult result = new BaseResult(ErrorCode.SUCCESS);
        CmbResult cmbResult = new CmbResult();
        cmbResult.setOrdNo("64");
        result.setData(cmbResult);
        return result;

    }
}
