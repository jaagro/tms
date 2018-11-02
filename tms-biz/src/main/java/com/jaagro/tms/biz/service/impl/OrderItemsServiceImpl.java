package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.dto.order.CreateOrderGoodsDto;
import com.jaagro.tms.api.dto.order.CreateOrderItemsDto;
import com.jaagro.tms.api.dto.order.GetOrderGoodsDto;
import com.jaagro.tms.api.dto.order.GetOrderItemsDto;
import com.jaagro.tms.api.service.OrderGoodsService;
import com.jaagro.tms.api.service.OrderItemsService;
import com.jaagro.tms.biz.entity.OrderGoodsMargin;
import com.jaagro.tms.biz.entity.OrderItems;
import com.jaagro.tms.biz.mapper.OrderGoodsMapperExt;
import com.jaagro.tms.biz.mapper.OrderGoodsMarginMapperExt;
import com.jaagro.tms.biz.mapper.OrderItemsMapperExt;
import com.jaagro.tms.biz.mapper.OrdersMapperExt;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author baiyiran
 */
@CacheConfig(keyGenerator = "wiselyKeyGenerator", cacheNames = "orderItems")
@Service
public class OrderItemsServiceImpl implements OrderItemsService {

    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private OrderItemsMapperExt orderItemsMapper;
    @Autowired
    private OrdersMapperExt ordersMapper;
    @Autowired
    private OrderGoodsMapperExt orderGoodsMapper;
    @Autowired
    private OrderGoodsMarginMapperExt orderGoodsMarginMapper;
    @Autowired
    private OrderGoodsService goodsService;
    @Autowired
    private CustomerClientService customerService;

    @CacheEvict(cacheNames = "orderItems", allEntries = true)
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
                goodsDto
                        .setOrderItemId(orderItem.getId())
                        .setOrderId(orderItem.getOrderId());
                this.goodsService.createOrderGood(goodsDto);
            }
        } else {
            throw new NullPointerException("订单明细不能为空");
        }
        return ServiceResult.toResult("创建成功");
    }

    @CacheEvict(cacheNames = "orderItems", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> updateItems(CreateOrderItemsDto itemsDto) {
        if (this.ordersMapper.selectByPrimaryKey(itemsDto.getOrderId()) == null) {
            throw new NullPointerException("订单明细不存在");
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

    @CacheEvict(cacheNames = "orderItems", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> disableByOrderId(Integer orderId) {
        if (ordersMapper.selectByPrimaryKey(orderId) == null) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "订单不存在");
        }
        List<OrderItems> orderItems = this.orderItemsMapper.listByOrderId(orderId);
        if (orderItems.size() < 1) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "订单明细不存在");
        }
        this.orderItemsMapper.disableByOrderId(orderId);
        // goods
        for (OrderItems items : orderItems
        ) {
            this.goodsService.disableByItemsId(items.getId());
        }
        return ServiceResult.toResult("删除成功");
    }

    @Cacheable
    @Override
    public List<GetOrderItemsDto> listByOrderId(Integer orderId) {
        List<GetOrderItemsDto> getOrderItemsDtoList = this.orderItemsMapper.listItemsByOrderId(orderId);
        if (getOrderItemsDtoList != null && getOrderItemsDtoList.size() > 0) {
            for (GetOrderItemsDto items : getOrderItemsDtoList) {
                OrderItems orderItems = this.orderItemsMapper.selectByPrimaryKey(items.getId());
                items
                        .setModifyUserId(this.currentUserService.getShowUser())
                        .setUnload(this.customerService.getShowSiteById(orderItems.getUnloadId()));
                for (GetOrderGoodsDto goodsDto : items.getGoods()) {
                    OrderGoodsMargin orderGoodsMarginData = orderGoodsMarginMapper.getMarginByGoodsId(goodsDto.getId());
                    if (null == orderGoodsMarginData) {
                        goodsDto.setMargin(new BigDecimal(0));
                    } else {
                        goodsDto.setMargin(orderGoodsMarginData.getMargin());
                    }
                }
            }
        }
        return getOrderItemsDtoList;
    }
}
