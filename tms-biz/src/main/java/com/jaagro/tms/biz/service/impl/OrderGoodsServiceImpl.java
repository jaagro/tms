package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.dto.order.CreateOrderGoodsDto;
import com.jaagro.tms.api.dto.order.GetOrderGoodsDto;
import com.jaagro.tms.api.service.OrderGoodsService;
import com.jaagro.tms.biz.entity.OrderGoods;
import com.jaagro.tms.biz.mapper.OrderGoodsMapper;
import com.jaagro.tms.biz.mapper.OrderGoodsMapperExt;
import com.jaagro.tms.biz.mapper.OrderItemsMapper;
import com.jaagro.tms.biz.mapper.OrderItemsMapperExt;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author baiyiran
 */
//@CacheConfig(keyGenerator = "wiselyKeyGenerator", cacheNames = "orderGoods")
@Service
public class OrderGoodsServiceImpl implements OrderGoodsService {

    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private OrderGoodsMapperExt orderGoodsMapper;
    @Autowired
    private OrderItemsMapperExt orderItemsMapper;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> updateGoods(CreateOrderGoodsDto goodsDto) {
        OrderGoods goods = this.orderGoodsMapper.selectByPrimaryKey(goodsDto.getId());
        if (goods == null) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "goods查询无数据");
        }
        goods
                .setModifyTime(new Date())
                .setModifyUserId(this.currentUserService.getShowUser().getId());
        this.orderGoodsMapper.updateByPrimaryKeySelective(goods);
        return ServiceResult.toResult("修改成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> disableByItemsId(Integer itemId) {
        this.orderGoodsMapper.disableByItemsId(itemId);
        return ServiceResult.toResult("取消成功");
    }

    /**
     * 根据订单需求id获得订单需求明细列表
     *
     * @param id
     * @return
     */
    @Override
    public List<GetOrderGoodsDto> listGoodsDtoByItemId(Integer id) {
        return orderGoodsMapper.listGoodsDtoByItemsId(id);
    }
}