package fun.onysakura.algorithm.core.mode.strategy;

import fun.onysakura.algorithm.core.mode.strategy.dto.PayConstants;
import fun.onysakura.algorithm.core.mode.strategy.dto.base.BaseResult;
import fun.onysakura.algorithm.core.mode.strategy.dto.cmb.CmbInitParam;
import fun.onysakura.algorithm.core.mode.strategy.strategy.Context;
import fun.onysakura.algorithm.core.mode.strategy.strategy.PayStrategy;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        PayStrategy cmbPayStrategy = Context.get(PayConstants.PayType.CMB);
        CmbInitParam initParam = new CmbInitParam();
        initParam.setTime(LocalDateTime.now());
        initParam.setOrderNo("123");
        initParam.setOrderName("qwe");
        initParam.setExpireTime(LocalDateTime.now());
        BaseResult initResult = cmbPayStrategy.getPayParams(initParam);
        System.out.println(initResult);
    }

}
