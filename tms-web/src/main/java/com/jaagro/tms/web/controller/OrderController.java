package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.order.CreateOrderDto;
import com.jaagro.tms.api.service.CustomerClientService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author baiyiran
 */
@RestController
@Api(description = "订单管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerClientService customerService;

    /**
     * 新增订单
     *
     * @param orderDto
     * @return
     */
    @ApiOperation("新增订单")
    @PostMapping("/order")
    public BaseResponse createOrder(@RequestBody CreateOrderDto orderDto) {
        if (StringUtils.isEmpty(orderDto.getCustomerId())) {
            return BaseResponse.service(ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户id不能为空"));
        }
        if (StringUtils.isEmpty(orderDto.getLoadSiteId())) {
            return BaseResponse.service(ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "收货id不能为空"));
        }
        if (StringUtils.isEmpty(orderDto.getCustomerContractId())) {
            return BaseResponse.service(ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户合同id不能为空"));
        }
        if (this.customerService.getShowCustomerById(orderDto.getCustomerId()) == null) {
            return BaseResponse.service(ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户不存在"));
        }
        if (this.customerService.getShowCustomerContractById(orderDto.getCustomerContractId()) == null) {
            return BaseResponse.service(ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户合同不存在"));
        }
        if (this.customerService.getShowSiteById(orderDto.getLoadSiteId()) == null) {
            return BaseResponse.service(ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "收货地址不存在"));
        }
        return BaseResponse.service(orderService.createOrder(orderDto));
    }
}
