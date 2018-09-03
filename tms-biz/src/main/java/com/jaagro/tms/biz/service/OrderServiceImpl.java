package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.order.*;
import com.jaagro.tms.api.service.CustomerClientService;
import com.jaagro.tms.api.service.OrderItemsService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.api.service.UserClientService;
import com.jaagro.tms.biz.entity.Orders;
import com.jaagro.tms.biz.mapper.OrdersMapper;
import com.jaagro.utils.ServiceResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserClientService userService;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private CustomerClientService customerService;
    @Autowired
    private OrderItemsService orderItemsService;

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
        //未完成
        /*order.setCreatedUserId(userService.getUserByToken("").getId());*/
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
        return null;
    }

    /**
     * 获取单条订单
     *
     * @param id 订单id
     * @return order对象
     */
    @Override
    public GetOrderDto getOrderById(Integer id) {
        return null;
    }

    /**
     * 分页获取订单列表
     *
     * @param criteriaDto 查询条件 json
     * @return 订单列表
     */
    @Override
    public List<ListOrderDto> listOrderByCriteria(ListOrderCriteriaDto criteriaDto) {
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
        return null;
    }
}
