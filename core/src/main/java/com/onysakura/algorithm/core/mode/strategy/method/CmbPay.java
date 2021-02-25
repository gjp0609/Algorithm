package com.onysakura.algorithm.core.mode.strategy.method;

import com.onysakura.algorithm.core.mode.strategy.dto.base.BaseInitParam;
import com.onysakura.algorithm.core.mode.strategy.dto.base.BaseResult;
import com.onysakura.algorithm.core.mode.strategy.dto.base.ErrorCode;
import com.onysakura.algorithm.core.mode.strategy.dto.cmb.CmbResult;
import com.onysakura.algorithm.core.mode.strategy.strategy.PayStrategy;

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
