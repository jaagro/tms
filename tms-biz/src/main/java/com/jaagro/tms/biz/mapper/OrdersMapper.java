package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.order.ListOrderCriteriaDto;
import com.jaagro.tms.api.dto.order.ListOrderDto;
import com.jaagro.tms.biz.entity.Orders;

import java.util.List;

public interface OrdersMapper {
    /**
     * @mbggenerated 2018-08-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-08-31
     */
    int insert(Orders record);

    /**
     * @mbggenerated 2018-08-31
     */
    int insertSelective(Orders record);

    /**
     * @mbggenerated 2018-08-31
     */
    Orders selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKeySelective(Orders record);

    /**
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKey(Orders record);

    /**
     * 查询订单列表
     *
     * @param criteriaDto
     * @return
     */
    List<Orders> listByCriteria(ListOrderCriteriaDto criteriaDto);

    /**
     * 分页查询 返回实体类包含详细信息
     *
     * @param criteriaDto
     * @return
     */
    List<ListOrderDto> listOrdersByCriteria(ListOrderCriteriaDto criteriaDto);

}