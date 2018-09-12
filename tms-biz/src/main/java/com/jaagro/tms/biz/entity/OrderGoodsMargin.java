package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class OrderGoodsMargin {
    /**
     * 
     */
    private Integer id;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单列表id
     */
    private Integer orderItemId;

    /**
     * 货物id
     */
    private Integer orderGoodsId;

    /**
     * 余量
     */
    private BigDecimal margin;
}