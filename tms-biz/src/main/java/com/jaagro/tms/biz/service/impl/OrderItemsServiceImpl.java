package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.dto.order.CreateOrderGoodsDto;
import com.jaagro.tms.api.dto.order.CreateOrderItemsDto;
import com.jaagro.tms.api.dto.order.GetOrderItemsDto;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.api.service.OrderGoodsService;
import com.jaagro.tms.api.service.OrderItemsService;
import com.jaagro.tms.biz.entity.OrderItems;
import com.jaagro.tms.biz.mapper.OrderGoodsMapper;
import com.jaagro.tms.biz.mapper.OrderItemsMapper;
import com.jaagro.tms.biz.mapper.OrdersMapper;
import com.jaagro.utils.ServiceResult;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author baiyiran
 */
@Service
public class OrderItemsServiceImpl implements OrderItemsService {

    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private OrderGoodsService goodsService;
    @Autowired
    private CustomerClientService customerService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createOrderItem(CreateOrderItemsDto orderItemDto) {
        if (this.customerService.getShowSiteById(orderItemDto.getUnloadId()) == null) {
            throw new NullPointerException("卸货地不存在");
        }
        OrderItems orderItem = new OrderItems();
        BeanUtils.copyProperties(orderItemDto, orderItem);
        this.orderItemsMapper.insertSelective(orderItem);
        if (orderItemDto.getGoods() != null && orderItemDto.getGoods().size() > 0) {
            for (CreateOrderGoodsDto goodsDto : orderItemDto.getGoods()
            ) {
                goodsDto.setOrderItemId(orderItem.getId());
                this.goodsService.createOrderGood(goodsDto);
            }
        } else {
            throw new NullPointerException("订单明细不能为空");
        }
        return ServiceResult.toResult("创建成功");
    }

    @Override
    public Map<String, Object> updateItems(CreateOrderItemsDto itemsDto) {
        if (this.ordersMapper.selectByPrimaryKey(itemsDto.getOrderId()) == null) {
            throw new RuntimeException("订单明细不存在");
        }
        OrderItems orderItems = new OrderItems();
        BeanUtils.copyProperties(itemsDto, orderItems);
        orderItems
                .setModifyTime(new Date())
                .setModifyUserId(this.currentUserService.getShowUser().getId());
        this.orderItemsMapper.updateByPrimaryKeySelective(orderItems);
        if (itemsDto.getGoods() != null && itemsDto.getGoods().size() > 0) {
            for (CreateOrderGoodsDto goodsDto : itemsDto.getGoods()
            ) {
                this.goodsService.updateGoods(goodsDto);
            }
        }
        return ServiceResult.toResult("修改成功");
    }

    @Override
    public Map<String, Object> disableById(Integer id) {
        OrderItems orderItems = this.orderItemsMapper.selectByPrimaryKey(id);
        if (orderItems == null) {
            return ServiceResult.error("删除失败");
        }
        orderItems.setEnabled(false);
        this.orderItemsMapper.updateByPrimaryKeySelective(orderItems);
        return ServiceResult.toResult("删除成功");
    }

    @Override
    public List<GetOrderItemsDto> listByOrderId(Integer orderId) {
        List<GetOrderItemsDto> getOrderItemsDtoList = this.orderItemsMapper.listItemsByOrderId(orderId);
        if (getOrderItemsDtoList != null && getOrderItemsDtoList.size() > 0) {
            for (GetOrderItemsDto items : getOrderItemsDtoList) {
                OrderItems orderItems = this.orderItemsMapper.selectByPrimaryKey(items.getId());
                items
                        .setModifyUserId(this.currentUserService.getShowUser())
                       .setModifyUserId(this.customerService.getShowCustomerById(orderItems.getModifyUserId()))
                        .setUnload(this.customerService.getShowSiteById(orderItems.getUnloadId()));

            }
        }
        return getOrderItemsDtoList;
    }
}
