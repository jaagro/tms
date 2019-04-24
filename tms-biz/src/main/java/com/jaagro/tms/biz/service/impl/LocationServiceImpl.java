package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.constant.OrderStatus;
import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
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
import com.jaagro.tms.biz.service.TruckClientService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

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
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private TruckClientService truckClientService;

    /**
     * 批量新增司机定位数据
     *
     * @param list
     * @return
     */
    @Override
    public int insertBatch(List<LocationDto> list) {
        log.info("O LocationServiceImpl.insertBatch ");
        ShowTruckDto truckDto = truckClientService.getTruckByToken();
        if (truckDto != null) {
            for (LocationDto locationDto : list) {
                locationDto.setDriverId(truckDto.getDrivers().get(0).getId());
                locationDto.setTruckId(truckDto.getId());
            }
        }
        int count = 0;
        if (!CollectionUtils.isEmpty(list)) {
            Collections.sort(list, Comparator.comparing(LocationDto::getLocationTime));
            count = locationMapper.insertBatch(list);
        }

        return count;
    }

    /**
     * 批量新增司机定位数据到MQ
     *
     * @param list
     */
    @Override
    public void insertBatchMQ(List<LocationDto> list) {

        ShowTruckDto truckDto = truckClientService.getTruckByToken();
        List<ShowDriverDto> driverList = truckDto.getDrivers();
        if (!CollectionUtils.isEmpty(driverList)) {
            Integer driverId = truckDto.getDrivers().get(0).getId();
            if (truckDto != null) {
                for (LocationDto locationDto : list) {
                    locationDto.setDriverId(driverId);
                    locationDto.setTruckId(truckDto.getId());
                }
            }

            amqpTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE, "location.send", list);
        }
    }


    /**
     * 监听司机定位数据并保存到数据库
     *
     * @param locationList
     * @return
     */
    @RabbitListener(queues = RabbitMqConfig.LOCATION_SEND_QUEUE)
    private void receiveMessage(List<LocationDto> locationList) {
        try {
            Collections.sort(locationList, Comparator.comparing(LocationDto::getLocationTime));
            if (!CollectionUtils.isEmpty(locationList)) {
                locationMapper.insertBatch(locationList);
            }
        } catch (Exception ex) {
            log.error("R LocationServiceImpl.receiveMessage params error{}", ex);
        }
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
        Assert.notNull(waybill, "运单不存在");
        Orders order = ordersMapperExt.selectByPrimaryKey(waybill.getOrderId());
        String key = "location:order:" + waybill.getOrderId() + ":" + waybill.getId();


        if (OrderStatus.ACCOMPLISH.equals(order.getOrderStatus()) || OrderStatus.TRANSPORT.equals(order.getOrderStatus())) {
            //订单的状态是运输中或者已完成
            String locationListJson = redis.get(key);
            if (StringUtils.isEmpty(locationListJson)) {
                locationDtos = locationMapper.listLocationsByWaybillId(waybillId, interval);
                if (!CollectionUtils.isEmpty(locationDtos)) {
                    redis.set(key, JsonUtils.objectToJson(locationDtos), 300);
                }
            } else {
                locationDtos = JsonUtils.jsonToList(locationListJson, ShowLocationDto.class);
            }
        } else {
            locationDtos = locationMapper.listLocationsByWaybillId(waybillId, interval);
        }

        return locationDtos;

    }

    public static void main(String[] args) {
        List<LocationDto> locationList = new ArrayList<>();
        LocationDto dto1 = new LocationDto();
        dto1.setWaybillId(1);
        dto1.setLocationTime(DateUtils.addSeconds(new Date(), 0));
        LocationDto dto2 = new LocationDto();
        dto2.setLocationTime(DateUtils.addSeconds(new Date(), 20));
        dto2.setWaybillId(2);
        LocationDto dto3 = new LocationDto();
        dto3.setLocationTime(DateUtils.addSeconds(new Date(), 4));
        dto3.setWaybillId(1);
        LocationDto dto4 = new LocationDto();
        dto4.setLocationTime(DateUtils.addSeconds(new Date(), 1));
        dto4.setWaybillId(3);
        locationList.add(dto3);
        locationList.add(dto2);
        locationList.add(dto1);
        locationList.add(dto4);
        Collections.sort(locationList, Comparator.comparing(LocationDto::getLocationTime));
        for (LocationDto locationDto : locationList) {
            System.out.println(locationDto.getWaybillId() + "===" + locationDto.getLocationTime());

        }
    }
}
