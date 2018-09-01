package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.customer.ShowCustomerContractDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.service.CustomerClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tony
 */
@RestController
public class TestController {

    @Autowired
    private CustomerClientService customerClientService;

    @GetMapping("/test1/{id}")
    public ShowCustomerDto test1(@PathVariable("id") Integer id){
        return customerClientService.getShowCustomerById(id);
    }

    @GetMapping("/test2/{id}")
    public ShowCustomerContractDto test2(@PathVariable("id") Integer id){
        return customerClientService.getShowCustomerContractById(id);
    }
}
