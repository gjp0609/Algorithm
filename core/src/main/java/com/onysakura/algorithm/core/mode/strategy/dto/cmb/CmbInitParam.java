package com.onysakura.algorithm.core.mode.strategy.dto.cmb;

import com.onysakura.algorithm.core.mode.strategy.dto.base.BaseInitParam;

import java.time.LocalDateTime;

public class CmbInitParam extends BaseInitParam {

    private LocalDateTime expireTime;

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "CmbInitParam{" +
                "expireTime=" + expireTime +
                "} " + super.toString();
    }
}
