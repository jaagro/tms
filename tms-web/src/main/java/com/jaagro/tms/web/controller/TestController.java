package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.waybill.LocationDto;
import com.jaagro.tms.biz.mapper.LocationMapperExt;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.impl.CurrentUserService;
import com.jaagro.tms.biz.service.impl.GpsLocationAsync;
import com.jaagro.utils.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author tony
 */
@Slf4j
@RestController
public class TestController {
    @Autowired
    CurrentUserService currentUserService;

    @Autowired
    LocationMapperExt locationMapper;


    @Autowired
    private GpsLocationAsync asyncTask;


    @GetMapping("/insertBatch")
    public void insertBatch() {
        long start = System.currentTimeMillis();
        List<LocationDto> list = new ArrayList<>();
        LocationDto loc;
        for (int i = 0; i < 500; i++) {
            loc = new LocationDto();
            loc.setLatitude(new BigDecimal(i));
            loc.setLongitude(new BigDecimal(i));
            loc.setLocationTime(new Date());
            list.add(loc);
        }

        int count = locationMapper.insertBatch(list);
        long end = System.currentTimeMillis();
        System.out.println("-----同步耗时----------" + (start - end) + "---------------");
        System.out.println("插入的条数：" + count);
    }

    @GetMapping("/asyncBatchInsert")
    public void asyncBatchInsert() {
        long start = System.currentTimeMillis();
        List<LocationDto> list = new ArrayList<>();
        LocationDto loc;
        for (int i = 0; i < 500; i++) {
            loc = new LocationDto();
            loc.setLatitude(new BigDecimal(i));
            loc.setLongitude(new BigDecimal(i));
            loc.setLocationTime(new Date());
            list.add(loc);
        }

        List<LocationDto> listA = list.subList(0, 165);
        List<LocationDto> listB = list.subList(165, 330);
        List<LocationDto> listC = list.subList(330, list.size());
        Future<Boolean> taskA = asyncTask.batchInsertOne(listA);
        Future<Boolean> taskB = asyncTask.batchInsertTwo(listB);
        Future<Boolean> taskC = asyncTask.batchInsertThree(listC);

        while (!taskA.isDone() || !taskB.isDone() || !taskC.isDone()) {
            if (taskA.isDone() && taskB.isDone() && taskC.isDone()) {
                break;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("-----异步耗时----------" + (start - end) + "---------------");
    }

    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;

            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<LocationDto> list = new ArrayList<>();
        LocationDto loc;
        for (int i = 0; i < 10210; i++) {
            loc = new LocationDto();
            loc.setLatitude(new BigDecimal(i));
            loc.setLongitude(new BigDecimal(i));
            loc.setLocationTime(new Date());
            list.add(loc);
        }
        List<List<LocationDto>> lll = averageAssign(list, 3);
        for (int i = 0; i < lll.size(); i++) {
            System.out.println(lll.get(i).size());
        }
        System.out.println("======================");
        System.out.println(lll.get(0).size());
        System.out.println(lll.get(1).size());
        System.out.println(lll.get(2).size());
        System.out.println(lll.get(4).size());
        long end = System.currentTimeMillis();
        System.out.println("-----耗时----------" + (start - end) + "---------------");
    }

}
