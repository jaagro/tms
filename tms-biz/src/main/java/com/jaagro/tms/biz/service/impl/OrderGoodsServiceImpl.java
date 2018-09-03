package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.dto.order.CreateOrderGoodsDto;
import com.jaagro.tms.api.service.OrderGoodsService;
import com.jaagro.tms.biz.entity.OrderGoods;
import com.jaagro.tms.biz.mapper.OrderGoodsMapper;
import com.jaagro.tms.biz.mapper.OrderItemsMapper;
import com.jaagro.utils.ServiceResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author baiyiran
 */
@Service
public class OrderGoodsServiceImpl implements OrderGoodsService {

    @Autowired
    private CurrentUserService currentUserService;
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
        if (goods == null) {
            return ServiceResult.toResult("删除失败");
        }
        goods.setEnabled(false);
        this.orderGoodsMapper.updateByPrimaryKeySelective(goods);
        return ServiceResult.toResult("删除成功");
    }

    @Override
    public Map<String, Object> updateGoods(CreateOrderGoodsDto goodsDto) {
        OrderGoods goods = this.orderGoodsMapper.selectByPrimaryKey(goodsDto.getId());
        if (goods != null) {
            return ServiceResult.toResult("修改失败");
        }
        goods
                .setModifyTime(new Date())
                .setModifyUserId(this.currentUserService.getShowUser().getId());
        this.orderGoodsMapper.updateByPrimaryKeySelective(goods);
        return ServiceResult.toResult("修改成功");
    }
}