package com.jaagro.tms.biz.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MiddleObjectVo {
    private Integer orderId;
    private Integer truckId;
    private Integer orderItemId;
    private Integer orderGoodsId;
    /**
     * 可配量
     */
    private Integer proportioning;
    /**
     * 已配量
     */
    private Integer planAmount;
    /**
     * 剩余量
     */
    private Integer unPlanAmount;
}
