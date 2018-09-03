package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.order.CreateOrderGoodsDto;
import com.jaagro.tms.api.service.OrderGoodsService;
import com.jaagro.tms.api.service.UserClientService;
import com.jaagro.tms.biz.entity.OrderGoods;
import com.jaagro.tms.biz.mapper.OrderGoodsMapper;
import com.jaagro.tms.biz.mapper.OrderItemsMapper;
import com.jaagro.utils.ServiceResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author baiyiran
 */
@Service
public class OrderGoodsServiceImpl implements OrderGoodsService {

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createOrderGood(CreateOrderGoodsDto orderGoodsDto) {
        if (this.orderItemsMapper.selectByPrimaryKey(orderGoodsDto.getOrderItemId()) == null) {
            throw new RuntimeException("订单明细不能为空");
        }
        OrderGoods goods = new OrderGoods();
        BeanUtils.copyProperties(orderGoodsDto, goods);
        this.orderGoodsMapper.insertSelective(goods);
        return ServiceResult.toResult("创建成功");
    }

    @Override
    public Map<String, Object> disableById(Integer id) {
        OrderGoods goods = this.orderGoodsMapper.selectByPrimaryKey(id);
        if (goods != null) {
            return ServiceResult.toResult("删除失败");
        }
        return null;
    }
}