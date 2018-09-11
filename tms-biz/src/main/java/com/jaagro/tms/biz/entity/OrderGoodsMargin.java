package com.jaagro.tms.biz.entity;

import java.math.BigDecimal;

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

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 订单id
     * @return order_id 订单id
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 订单id
     * @param orderId 订单id
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 订单列表id
     * @return order_item_id 订单列表id
     */
    public Integer getOrderItemId() {
        return orderItemId;
    }

    /**
     * 订单列表id
     * @param orderItemId 订单列表id
     */
    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    /**
     * 货物id
     * @return order_goods_id 货物id
     */
    public Integer getOrderGoodsId() {
        return orderGoodsId;
    }

    /**
     * 货物id
     * @param orderGoodsId 货物id
     */
    public void setOrderGoodsId(Integer orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    /**
     * 余量
     * @return margin 余量
     */
    public BigDecimal getMargin() {
        return margin;
    }

    /**
     * 余量
     * @param margin 余量
     */
    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }
}