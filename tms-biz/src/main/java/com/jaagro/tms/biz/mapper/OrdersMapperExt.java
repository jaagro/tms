package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.order.ListOrderCriteriaDto;
import com.jaagro.tms.api.dto.order.ListOrderDto;
import com.jaagro.tms.api.dto.order.WeChatListOrderDto;

import java.util.List;

/**
 * @author tony
 */
public interface OrdersMapperExt extends OrdersMapper {

    /**
     * 分页查询 返回实体类包含详细信息
     *
     * @param criteriaDto
     * @return
     */
    List<ListOrderDto> listOrdersByCriteria(ListOrderCriteriaDto criteriaDto);

    /**
     * 根据客户id查询订单id数组
     *
     * @param customerId
     * @return
     */
    List<Integer> getOrderIdsByCustomerId(Integer customerId);

    /**
     * 微信小程序 订单列表
     *
     * @param criteriaDto
     * @return
     */
    List<WeChatListOrderDto> listWeChatOrdersByCriteria(ListOrderCriteriaDto criteriaDto);
}