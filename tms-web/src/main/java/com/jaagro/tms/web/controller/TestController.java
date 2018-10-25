package com.jaagro.tms.web.controller;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerContractDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.dto.waybill.CreateWaybillPlanDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillDetailDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillDto;
import com.jaagro.tms.api.dto.waybill.ListWaybillPlanDto;
import com.jaagro.tms.api.service.WaybillPlanService;
import com.jaagro.tms.api.service.WaybillRefactorService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.SmsClientService;
import com.jaagro.tms.biz.service.TruckClientService;
import com.jaagro.tms.biz.service.TruckTypeClientService;
import com.jaagro.tms.biz.service.impl.CurrentUserService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.BeanDifferentUtils;
import com.jaagro.utils.DifferentResult;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private WaybillService waybillService;
    @Autowired
    private WaybillPlanService waybillPlanService;
    @Autowired
    private SmsClientService smsClientService;
    @Autowired
    private WaybillRefactorService waybillRefactorService;

    @GetMapping("/test1/{id}")
    public ShowCustomerDto test1(@PathVariable("id") Integer id){
        return customerClientService.getShowCustomerById(id);
    }

    @GetMapping("/test2/{id}")
    public ShowCustomerContractDto test2(@PathVariable("id") Integer id){
        return customerClientService.getShowCustomerContractById(id);
    }

    @GetMapping("/test3")
    public UserInfo test3(){
        return currentUserService.getCurrentUser();
    }

    @GetMapping("/test4")
    public DifferentResult test4(){
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginName("hello");
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setName("world");
        DifferentResult result = BeanDifferentUtils.compare(userInfo, userInfo1);
        return result;
    }

    @GetMapping("/test5")
    public List<ListTruckTypeDto> test5(){
        return truckTypeClientService.listTruckTypeReturnDto();
    }
    @GetMapping("/test6/{id}")
    public ListTruckTypeDto test56(@PathVariable("id") Integer id){
        return truckTypeClientService.getTruckTypeById(id);
    }
    @PostMapping("/createWaybillPlan")
    public List<ListWaybillPlanDto> createWaybill(@RequestBody CreateWaybillPlanDto waybillPlanDto){
        return waybillPlanService.createWaybillPlan(waybillPlanDto);
    }

//    @GetMapping("/test7/{orderId}")
//    public List<GetWaybillDto> test7(@PathVariable("orderId") Integer orderId){
//        return waybillService.listWaybillByOrderId(orderId);
//    }
    @Autowired
    private TruckClientService truckClientService;

    @GetMapping("/test8/{truckId}")
    public ShowTruckDto test8(@PathVariable("truckId") Integer truckId){
        return truckClientService.getTruckByIdReturnObject(truckId);
    }

    @GetMapping("/test9/{id}")
    public GetWaybillDto test9(@PathVariable("id") Integer id){
        return waybillService.getWaybillById(id);
    }

    @GetMapping("/test10/{id}/{userType}")
    public UserInfo test10(@PathVariable("id") Integer id, @PathVariable("userType") String userType){
        return currentUserService.getUserInfoById(id, userType);
    }

    @GetMapping("/test11")
    public BaseResponse test11(){
        try{
        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("drvierName","driver.getName()");
            BaseResponse response=smsClientService.sendSMS("13600517630","smsTemplate_assignWaybill", templateMap);
            System.out.println("TestHello=============================++++++++++++++");
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author Gavin
     *
     * @param orderId
     * @return
     */
    @ApiOperation("根据订单号查询运单详情列表")
    @PostMapping("/listWaybillDetailByOrderId/{orderId}")
    public BaseResponse listWaybillDetailByOrderId(@PathVariable Integer orderId) {
        try {
            List<GetWaybillDetailDto> waybillDetailDtos = waybillRefactorService.listWaybillDetailByOrderId(orderId);
            return BaseResponse.successInstance(waybillDetailDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.errorInstance(ResponseStatusCode.SERVER_ERROR.getCode(), "查询运单详情列表失败:" + e.getMessage());
        }
    }

    /**
     * 根据id查询运单相关的所有对象
     * @Author Gavin
     * @param waybillId
     * @return
     */
    @ApiOperation("根据运单号查询运单详情")
    @PostMapping("/getWaybillDetailById/{waybillId}")
    public BaseResponse getWaybillDetailById(@PathVariable Integer waybillId) {
        try {
            GetWaybillDetailDto waybillDetailDto = waybillRefactorService.getWaybillDetailById(waybillId);
            return BaseResponse.successInstance(waybillDetailDto);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.errorInstance(ResponseStatusCode.SERVER_ERROR.getCode(), "查询运单详情失败:" + e.getMessage());
        }
    }
}