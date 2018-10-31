package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.base.GetCustomerUserDto;
import com.jaagro.tms.biz.service.impl.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tony
 */
@RestController
public class TestController {
    @Autowired
    CurrentUserService currentUserService;

    @GetMapping("/test")
    public GetCustomerUserDto test1() {
        return currentUserService.getCustomerUserById();
    }
}
