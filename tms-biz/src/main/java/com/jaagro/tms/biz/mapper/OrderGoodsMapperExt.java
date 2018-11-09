package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.order.GetOrderDto;
import com.jaagro.tms.api.dto.order.GetOrderGoodsDto;
import com.jaagro.tms.biz.entity.OrderGoods;

import java.io.Serializable;
import java.util.List;

/**
 * @author tony
 */
public interface OrderGoodsMapperExt extends OrderGoodsMapper {

    /**
     * 根据订单需求id查询列表
     *
     * @param id
     * @return
     */
    List<OrderGoods> listByItemsId(Integer id);

    /**
     * 根据订单需求id查询列表
     *
     * @param id
     * @return
     */
    List<GetOrderGoodsDto> listGoodsDtoByItemsId(Integer id);

    /**
     * 根据orderItemsId删除
     *
     * @param id
     */
    int disableByItemsId(Integer id);
}