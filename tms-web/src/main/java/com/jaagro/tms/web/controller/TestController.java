package com.jaagro.tms.web.controller;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerContractDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.service.OrderModifyLogService;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.TruckTypeClientService;
import com.jaagro.tms.biz.service.impl.CurrentUserService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.BeanDifferentUtils;
import com.jaagro.utils.DifferentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author tony
 */
@RestController
public class TestController {

    @Autowired
    private CustomerClientService customerClientService;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private TruckTypeClientService truckTypeClientService;
    @Autowired
    private OrderModifyLogService modifyLogService;

    @GetMapping("/test1/{id}")
    public ShowCustomerDto test1(@PathVariable("id") Integer id) {
        return customerClientService.getShowCustomerById(id);
    }

    @GetMapping("/test2/{id}")
    public ShowCustomerContractDto test2(@PathVariable("id") Integer id) {
        return customerClientService.getShowCustomerContractById(id);
    }

    @GetMapping("/test3")
    public UserInfo test3() {
        return currentUserService.getCurrentUser();
    }

    @GetMapping("/test4")
    public DifferentResult test4() {
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginName("hello");
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setName("world");
        DifferentResult result = BeanDifferentUtils.compare(userInfo, userInfo1);
        return result;
    }

    @GetMapping("/test4-1")
    public BaseResponse test41() {
        return BaseResponse.service(this.modifyLogService.createModifyLog(null));
    }

    @GetMapping("/test5")
    public List<ListTruckTypeDto> test5() {
        return truckTypeClientService.listTruckTypeReturnDto();
    }
}