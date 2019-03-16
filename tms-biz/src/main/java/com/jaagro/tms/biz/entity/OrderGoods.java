package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class OrderGoods implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     *
     */
    private Integer orderItemId;

    /**
     * 货物名称
     */
    private String goodsName;

    /**
     * 货物单位：1-羽 2-头 3-吨
     */
    private Integer goodsUnit;

    /**
     * 货物数量
     */
    private Integer goodsQuantity;

    /**
     * 货物重量
     */
    private BigDecimal goodsWeight;

    /**
     * 饲料类型：1-散装 2-袋装 (仅饲料情况下)
     */
    private Integer feedType;

    /**
     * 是否加药
     */
    private Boolean joinDrug;

    /**
     * 是否有效
     */
    private Boolean enabled;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人id
     */
    private Integer modifyUserId;

}