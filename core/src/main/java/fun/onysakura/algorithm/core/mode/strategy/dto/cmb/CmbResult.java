package fun.onysakura.algorithm.core.mode.strategy.dto.cmb;

public class CmbResult {

    private String ordNo;

    public String getOrdNo() {
        return ordNo;
    }

    public void setOrdNo(String ordNo) {
        this.ordNo = ordNo;
    }

    @Override
    public String toString() {
        return "CmbResult{" +
                "ordNo='" + ordNo + '\'' +
                '}';
    }
}
