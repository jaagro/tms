package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.order.ListOrderCriteriaDto;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author baiyiran
 */
@RestController
@Api(description = "微信小程序订单管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class WeChatAppletOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 查询单条订单
     *
     * @param id
     * @return
     */
    @ApiOperation("查询单条订单")
    @GetMapping("/getWechatOrderById/{id}")
    public BaseResponse getOrderById(@PathVariable("id") Integer id) {
        return BaseResponse.successInstance(orderService.getOrderById(id));
    }

    /**
     * 分页查询订单
     *
     * @param criteriaDto
     * @return
     */
    @ApiOperation("分页查询订单")
    @PostMapping("/listWechatOrders")
    public BaseResponse listOrders(@RequestBody ListOrderCriteriaDto criteriaDto) {
        if (StringUtils.isEmpty(criteriaDto.getPageNum())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageNum不能为空");
        }
        if (StringUtils.isEmpty(criteriaDto.getPageSize())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageSize不能为空");
        }
        return BaseResponse.service(orderService.listOrderByCriteria(criteriaDto));
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param detailInfo
     * @return
     */
    @ApiOperation("取消订单")
    @PostMapping("/cancelWechatOrder/{orderId}/{detailInfo}")
    public BaseResponse cancelOrders(@PathVariable("orderId") Integer orderId, @PathVariable("detailInfo") String detailInfo) {
        if (StringUtils.isEmpty(orderId)) {
            return BaseResponse.idNull("订单id不能为空");
        }
        if (StringUtils.isEmpty(detailInfo)) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "取消理由必填");
        }
        return BaseResponse.service(orderService.cancelOrders(orderId, detailInfo));
    }

}
