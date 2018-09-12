package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.OrderGoodsMargin;

/**
 * @author tony
 */
public interface OrderGoodsMarginMapperExt extends OrderGoodsMarginMapper{

    /**
     * 根据订单id获取货物余量
     * @param orderGoodsId
     * @return
     */
    OrderGoodsMargin getMarginByGoodsId(Integer orderGoodsId);
}
