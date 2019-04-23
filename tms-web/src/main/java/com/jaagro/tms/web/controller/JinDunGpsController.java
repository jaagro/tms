package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.service.JinDunGpsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description: 金盾车辆轨迹管理
 * @author: @Gao.
 * @create: 2019-04-23 14:21
 **/
@Slf4j
@RestController
@Api(description = "金盾设备管理器", produces = MediaType.APPLICATION_JSON_VALUE)
public class JinDunGpsController {
    @Autowired
    private JinDunGpsService jinDunGpsService;

    @ApiOperation("金盾系统登录")
    @PostMapping("/jinDunLogin")
    public void jinDunLogin() {
        jinDunGpsService.jinDunLogin();
    }
}
