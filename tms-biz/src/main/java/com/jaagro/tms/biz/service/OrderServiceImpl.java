package com.jaagro.tms.biz.service;

import com.github.pagehelper.PageHelper;
import com.jaagro.tms.api.constant.OrderStatus;
import com.jaagro.tms.api.dto.order.*;
import com.jaagro.tms.api.service.CustomerClientService;
import com.jaagro.tms.api.service.OrderGoodsService;
import com.jaagro.tms.api.service.OrderItemsService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.biz.entity.OrderGoods;
import com.jaagro.tms.biz.entity.OrderItems;
import com.jaagro.tms.biz.entity.Orders;
import com.jaagro.tms.biz.mapper.OrderGoodsMapper;
import com.jaagro.tms.biz.mapper.OrderItemsMapper;
import com.jaagro.tms.biz.mapper.OrdersMapper;
import com.jaagro.utils.ServiceResult;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author tony
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private CustomerClientService customerService;
    @Autowired
    private OrderItemsService orderItemsService;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private OrderGoodsService orderGoodsService;

    /**
     * 创建订单
     *
     * @param orderDto 入参json
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override

    public Map<String, Object> createOrder(CreateOrderDto orderDto) {
        Orders order = new Orders();
        BeanUtils.copyProperties(orderDto, order);
        order.setCreatedUserId(currentUserService.getShowUser().getId());
        this.ordersMapper.insertSelective(order);
        if (orderDto.getOrderItems() != null && orderDto.getOrderItems().size() > 0) {
            for (CreateOrderItemsDto itemsDto : orderDto.getOrderItems()
            ) {
                itemsDto.setOrderId(order.getId());
                this.orderItemsService.createOrderItem(itemsDto);
            }
        } else {
            throw new RuntimeException("订单明细不能为空");
        }
        return ServiceResult.toResult("创建成功");
    }

    /**
     * 修改订单
     *
     * @param orderDto 入参json
     * @return 修改后的order对象
     */
    @Override
    public GetOrderDto updateOrder(UpdateOrderDto orderDto) {
        //修改order
        Orders orders = new Orders();
        BeanUtils.copyProperties(orderDto, orders);
        orders
                .setModifyTime(new Date())
                .setModifyUserId(this.currentUserService.getShowUser().getId());
        this.ordersMapper.updateByPrimaryKeySelective(orders);

        if (orderDto.getOrderItems() != null && orderDto.getOrderItems().size() > 0) {
            for (CreateOrderItemsDto itemsDto : orderDto.getOrderItems()
            ) {
                this.orderItemsService.updateItems(itemsDto);
            }
        }

        //返回
        Orders order = this.ordersMapper.selectByPrimaryKey(orderDto.getId());
        GetOrderDto getOrderDto = new GetOrderDto();
        BeanUtils.copyProperties(order, orderDto);
        getOrderDto
                .setCustomer(this.customerService.getShowCustomerById(order.getCustomerId()))
                .setCreatedUser(this.currentUserService.getShowUser())
                .setCustomerContract(this.customerService.getShowCustomerContractById(order.getCustomerContractId()))
                .setLoadSiteId(this.customerService.getShowSiteById(order.getLoadSiteId()));
        return getOrderDto;
    }

    /**
     * 获取单条订单
     *
     * @param id 订单id
     * @return order对象
     */
    @Override
    public GetOrderDto getOrderById(Integer id) {
        Orders order = this.ordersMapper.selectByPrimaryKey(id);
        GetOrderDto orderDto = new GetOrderDto();
        BeanUtils.copyProperties(order, orderDto);
        orderDto
                .setCustomer(this.customerService.getShowCustomerById(order.getCustomerId()))
                .setCreatedUser(this.currentUserService.getShowUser())
                .setCustomerContract(this.customerService.getShowCustomerContractById(order.getCustomerContractId()))
                .setLoadSiteId(this.customerService.getShowSiteById(order.getLoadSiteId()));
        return orderDto;
    }

    /**
     * 分页获取订单列表
     *
     * @param criteriaDto 查询条件 json
     * @return 订单列表
     */
    @Override
    public List<ListOrderDto> listOrderByCriteria(ListOrderCriteriaDto criteriaDto) {
        PageHelper.startPage(criteriaDto.getPageNum(), criteriaDto.getPageSize());
        List<Orders> orderDtos = this.ordersMapper.listByCriteria(criteriaDto);
        List<ListOrderDto> listOrderDtos = new ArrayList<>();
        BeanUtils.copyProperties(orderDtos, listOrderDtos);
        if (listOrderDtos != null && listOrderDtos.size() > 0) {
            for (Orders order : orderDtos
            ) {
                ListOrderDto orderDto = new ListOrderDto();
                BeanUtils.copyProperties(order, orderDto);
                orderDto
                        .setCustomerId(this.customerService.getShowCustomerById(order.getCustomerId()))
                        .setCreatedUserId(this.currentUserService.getShowUser())
                        .setCustomerContract(this.customerService.getShowCustomerContractById(order.getCustomerContractId()))
                        .setLoadSite(this.customerService.getShowSiteById(order.getLoadSiteId()));
            }
        }
        return null;
    }

    /**
     * 【逻辑】删除订单
     *
     * @param id 订单id
     * @return
     */
    @Override
    public Map<String, Object> deleteOrderById(Integer id) {
        Orders orders = this.ordersMapper.selectByPrimaryKey(id);
        if (orders == null) {
            throw new NullPointerException("订单不存在");
        }
        orders.setOrderStatus(OrderStatus.CANCEL);
        ordersMapper.updateByPrimaryKeySelective(orders);
        List<OrderItems> orderItems = this.orderItemsMapper.listByOrderId(orders.getId());
        if (orderItems != null) {
            for (OrderItems items : orderItems
            ) {
                this.orderItemsService.disableById(items.getId());
                List<OrderGoods> orderGoods = this.orderGoodsMapper.listByItemsId(items.getId());
                if (orderGoods != null) {
                    for (OrderGoods goods : orderGoods
                    ) {
                        this.orderGoodsService.disableById(goods.getId());
                    }
                }
            }
        }
        return ServiceResult.toResult("删除成功");
    }
}
