package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.order.CreateOrderDto;
import com.jaagro.tms.api.dto.order.ListOrderCriteriaDto;
import com.jaagro.tms.api.dto.order.UpdateOrderDto;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.biz.mapper.OrdersMapper;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


/**
 * @author baiyiran
 */
@Aspect
@RestController
@Api(description = "订单管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerClientService customerService;
    @Autowired
    private OrdersMapper ordersMapper;

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
        return BaseResponse.service(orderService.createOrder(orderDto));
    }

    /**
     * 修改订单
     *
     * @param orderDto
     * @return
     */
    @ApiOperation("修改订单")
    @PutMapping("/order")
    public BaseResponse updateOrder(@RequestBody UpdateOrderDto orderDto) {
        if (StringUtils.isEmpty(orderDto.getCustomerId())) {
            return BaseResponse.service(ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户id不能为空"));
        }
        if (this.ordersMapper.selectByPrimaryKey(orderDto.getCustomerId()) == null) {
            return BaseResponse.service(ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "订单不存在"));
        }
        return BaseResponse.successInstance(orderService.updateOrder(orderDto));
    }

    /**
     * 删除订单
     *
     * @param id
     * @return
     */
    @ApiOperation("删除订单")
    @DeleteMapping("/order")
    public BaseResponse deleteOrder(@PathVariable Integer id) {
        if (this.ordersMapper.selectByPrimaryKey(id) == null) {
            return BaseResponse.service(ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "订单不存在"));
        }
        return BaseResponse.service(orderService.deleteOrderById(id));
    }

    /**
     * 查询单条订单
     *
     * @param id
     * @return
     */
    @ApiOperation("查询单条订单")
    @GetMapping("/getOrderById/{id}")
    public BaseResponse getOrderById(@PathVariable("id") Integer id) {
        if (this.ordersMapper.selectByPrimaryKey(id) == null) {
            return BaseResponse.service(ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "订单不存在"));
        }
        return BaseResponse.successInstance(orderService.getOrderById(id));
    }

    /**
     * 分页查询订单
     *
     * @param criteriaDto
     * @return
     */
    @ApiOperation("分页查询订单")
    @PostMapping("/listOrders")
    public BaseResponse listOrders(@RequestBody ListOrderCriteriaDto criteriaDto) {
        return BaseResponse.service(orderService.listOrderByCriteria(criteriaDto));
    }
}
