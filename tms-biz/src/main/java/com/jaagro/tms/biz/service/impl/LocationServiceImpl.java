package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.constant.OrderStatus;
import com.jaagro.tms.api.dto.waybill.LocationDto;
import com.jaagro.tms.api.dto.waybill.ShowLocationDto;
import com.jaagro.tms.api.service.LocationService;
import com.jaagro.tms.biz.common.JsonUtils;
import com.jaagro.tms.biz.common.RedisOperator;
import com.jaagro.tms.biz.config.RabbitMqConfig;
import com.jaagro.tms.biz.entity.Orders;
import com.jaagro.tms.biz.entity.Waybill;
import com.jaagro.tms.biz.mapper.LocationMapperExt;
import com.jaagro.tms.biz.mapper.OrdersMapperExt;
import com.jaagro.tms.biz.mapper.WaybillMapperExt;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Log4j
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationMapperExt locationMapper;
    @Autowired
    private WaybillMapperExt waybillMapper;
    @Autowired
    private OrdersMapperExt ordersMapperExt;

    @Autowired
    private RedisOperator redis;


    /**
     * 批量新增司机定位数据
     *
     * @param locationList
     * @return
     */
    @Override
    public int insertBatch(List<LocationDto> locationList) {

        int count = locationMapper.insertBatch(locationList);

        return count;
    }
    /**
     * 监听司机定位数据并保存到数据库
     *
     * @param locationList
     * @return
     */
    @RabbitListener(queues = RabbitMqConfig.LOCATION_SEND_QUEUE)
    private void receiveMessage(List<LocationDto> locationList) {
        locationMapper.insertBatch(locationList);
    }

    /**
     * 根据运单Id查询轨迹
     *
     * @param waybillId
     * @return
     */
    @Override
    public List<ShowLocationDto> locationsByWaybillId(Integer waybillId, Integer interval) {
        List<ShowLocationDto> locationDtos;
        Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
        Orders order = ordersMapperExt.selectByPrimaryKey(waybill.getOrderId());
        String key = "location:order:" + waybill.getOrderId() + ":" + waybill.getId();


        if (OrderStatus.ACCOMPLISH.equals(order.getOrderStatus()) || OrderStatus.TRANSPORT.equals(order.getOrderStatus())) {
            //订单的状态是运输中或者已完成
            String locationListJson = redis.get(key);
            if (StringUtils.isEmpty(locationListJson)) {
                locationDtos = locationMapper.listLocationsByWaybillId(waybillId, interval);
                if (!CollectionUtils.isEmpty(locationDtos)) {
                    redis.set(key, JsonUtils.objectToJson(locationDtos), 2000);
                }
            } else {
                locationDtos = JsonUtils.jsonToList(locationListJson, ShowLocationDto.class);
            }
        } else {
            locationDtos = locationMapper.listLocationsByWaybillId(waybillId, interval);
        }


        return locationDtos;

    }


}
