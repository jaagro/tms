package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.base.GetCustomerUserDto;
import com.jaagro.tms.biz.entity.Location;
import com.jaagro.tms.biz.mapper.LocationMapperExt;
import com.jaagro.tms.biz.service.impl.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tony
 */
@RestController
public class TestController {
    @Autowired
    CurrentUserService currentUserService;

    @Autowired
    LocationMapperExt locationMapper;

    @GetMapping("/test")
    public GetCustomerUserDto test1() {
        return currentUserService.getCustomerUserById();
    }


    @GetMapping("/insertBatch")
    public void insertBatch() {
        long start = System.currentTimeMillis();
        List<Location> list = new ArrayList<>();
        Location loc;
        for (int i = 0; i < 400; i++) {
            loc = new Location();
            loc.setLatitude(new BigDecimal(i));
            loc.setLongitude(new BigDecimal(i));
            loc.setLocationTime(new Date());
            list.add(loc);
        }

        int count= locationMapper.insertBatch(list);
        long end = System.currentTimeMillis();
        System.out.println("-----耗时----------" + (start - end) + "---------------");
        System.out.println("插入的条数："+count);
    }
}
