package fun.onysakura.algorithm.core.mode.strategy.dto.base;

import java.time.LocalDateTime;

public class BaseInitParam {

    private LocalDateTime time;
    private String orderNo;
    private String orderName;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }


    @Override
    public String toString() {
        return "BaseInitParam{" +
                "time=" + time +
                ", orderNo='" + orderNo + '\'' +
                ", orderName='" + orderName + '\'' +
                '}';
    }
}
