package com.taiping.bean.riskmanage;

import com.taiping.entity.riskmanage.RiskItem;

/**
 * @author zhangliangyu
 * @since 2019/10/28
 * 风险项dto
 */
public class RiskItemDto  extends RiskItem {
    /**
     * 风险追踪负责人姓名
     */
    private String trackUserName;
    /**
     * 复检人姓名
     */
    private String checkUserName;

    public String getTrackUserName() {
        return trackUserName;
    }

    public void setTrackUserName(String trackUserName) {
        this.trackUserName = trackUserName;
    }

    public String getCheckUserName() {
        return checkUserName;
    }

    public void setCheckUserName(String checkUserName) {
        this.checkUserName = checkUserName;
    }
}
