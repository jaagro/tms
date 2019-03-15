package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.constant.GoodsType;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.order.*;
import com.jaagro.tms.api.service.OrderGoodsService;
import com.jaagro.tms.api.service.OrderItemsService;
import com.jaagro.tms.biz.entity.OrderGoodsMargin;
import com.jaagro.tms.biz.entity.OrderItems;
import com.jaagro.tms.biz.entity.Orders;
import com.jaagro.tms.biz.mapper.OrderGoodsMapperExt;
import com.jaagro.tms.biz.mapper.OrderGoodsMarginMapperExt;
import com.jaagro.tms.biz.mapper.OrderItemsMapperExt;
import com.jaagro.tms.biz.mapper.OrdersMapperExt;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createOrderItem(CreateOrderItemsDto orderItemDto) {
        /*if (this.customerService.getShowSiteById(orderItemDto.getUnloadId()) == null) {
            throw new NullPointerException("卸货地不存在");
        }*/
        OrderItems orderItem = new OrderItems();
        BeanUtils.copyProperties(orderItemDto, orderItem);
        this.orderItemsMapper.insertSelective(orderItem);
        Orders order = ordersMapper.selectByPrimaryKey(orderItem.getOrderId());
        if (order == null) {
            throw new NullPointerException("订单不存在");
        }
        if (orderItemDto.getGoods() != null && orderItemDto.getGoods().size() > 0) {
            for (CreateOrderGoodsDto goodsDto : orderItemDto.getGoods()
            ) {
                if (GoodsType.FODDER.equals(order.getGoodsType())) {
//                    if (StringUtils.isEmpty(goodsDto.getFeedType())) {
//                        throw new NullPointerException("饲料类型不能为空");
//                    }
                }
                goodsDto
                        .setId(null)
                        .setOrderItemId(orderItem.getId())
                        .setOrderId(orderItem.getOrderId());
                this.goodsService.createOrderGood(goodsDto);
            }
        } else {
            throw new NullPointerException("订单明细不能为空");
        }
        return ServiceResult.toResult("创建成功");
    }

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

    /**
     * 根据订单id获得订单需求列表
     *
     * @param orderId
     * @return
     */
    @Override
    public List<ListOrderItemsDto> listItemsByOrderId(Integer orderId) {
        List<ListOrderItemsDto> orderItemsDtoList = this.orderItemsMapper.listItemsDtoByOrderId(orderId);
        if (orderItemsDtoList.size() > 0) {
            for (ListOrderItemsDto items : orderItemsDtoList) {
//                OrderItems orderItems = this.orderItemsMapper.selectByPrimaryKey(items.getId());
                /*items
                        .setModifyUserId(this.currentUserService.getShowUser())
                        .setUnload(this.customerService.getShowSiteById(orderItems.getUnloadId()));*/
                List<GetOrderGoodsDto> goodsDtoList = goodsService.listGoodsDtoByItemId(items.getId());
                items.setOrderGoodsDtoList(goodsDtoList);
                /*for (GetOrderGoodsDto goodsDto : items.getGoods()) {
                    OrderGoodsMargin orderGoodsMarginData = orderGoodsMarginMapper.getMarginByGoodsId(goodsDto.getId());
                    if (null == orderGoodsMarginData) {
                        goodsDto.setMargin(new BigDecimal(0));
                    } else {
                        goodsDto.setMargin(orderGoodsMarginData.getMargin());
                    }
                }*/
            }
        }
        return orderItemsDtoList;
    }

    /**
     * 根据订单id删除orderItems
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteByOrderId(Integer orderId) {
        Boolean result = false;
        int msg = orderItemsMapper.deleteByOrderId(orderId);
        if (msg > 0) {
            //删除货物详细信息goods
            Boolean goodsResult = goodsService.deleteByOrderId(orderId);
            if (goodsResult) {
                result = true;
                return result;
            }
            return result;
        }
        return result;
    }
}
