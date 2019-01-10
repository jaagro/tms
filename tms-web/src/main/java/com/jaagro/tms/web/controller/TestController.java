package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.waybill.ImportWaybillDto;
import com.jaagro.tms.api.dto.waybill.LocationDto;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.mapper.LocationMapperExt;
import com.jaagro.tms.biz.service.impl.CurrentUserService;
import com.jaagro.tms.biz.service.impl.GpsLocationAsync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private WaybillService waybillService;


    @Autowired
    private GpsLocationAsync asyncTask;

    @GetMapping("/importWaybillsForChilken")
    public void importChilkenWaybill() {
        List<ImportWaybillDto> importDtos = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String loadtime1 = "2019-01-11 02:55:00";
        String requiredtime1 = "2019-01-11 04:40:00";
        try {
            ImportWaybillDto importDto1 = new ImportWaybillDto();
            importDto1.setOrderId(935);
            importDto1.setCustomerId(359);
            importDto1.setGoodsQuantity(324);
            importDto1.setLoadSiteId(3916);
            importDto1.setLoadTime(sdf.parse(loadtime1));
            importDto1.setLoadSiteDeptId(156);
            importDto1.setRequiredTime(sdf.parse(requiredtime1));
            importDto1.setTruckId(459);
            importDto1.setTruckNumber("8899");
            importDto1.setTruckTypeId(8);
            importDto1.setTruckTeamContractId(11);
            importDtos.add(importDto1);
            String loadtime2 = "2019-01-11 05:30:00";
            String requiredtime2 = "2019-01-11 07:15:00\n";
            ImportWaybillDto importDto2 = new ImportWaybillDto();
            importDto2.setOrderId(935);
            importDto2.setCustomerId(359);
            importDto2.setGoodsQuantity(324);
            importDto2.setLoadSiteId(3916);
            importDto2.setLoadTime(sdf.parse(loadtime2));
            importDto2.setLoadSiteDeptId(156);
            importDto2.setRequiredTime(sdf.parse(requiredtime2));
            importDto2.setTruckId(459);
            importDto2.setTruckNumber("8899");
            importDto2.setTruckTypeId(8);
            importDto2.setTruckTeamContractId(11);
            importDtos.add(importDto2);

            waybillService.importWaybills(935, importDtos);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

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
