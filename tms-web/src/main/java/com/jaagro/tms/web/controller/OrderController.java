package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.constant.OrderStatus;
import com.jaagro.tms.api.dto.customer.CustomerContactsReturnDto;
import com.jaagro.tms.api.dto.order.CreateOrderDto;
import com.jaagro.tms.api.dto.order.GetOrderDto;
import com.jaagro.tms.api.dto.order.ListOrderCriteriaDto;
import com.jaagro.tms.api.dto.order.UpdateOrderDto;
import com.jaagro.tms.api.service.OrderRefactorService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.biz.mapper.OrdersMapper;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.web.vo.order.CustomerContactsVo;
import com.jaagro.tms.web.vo.order.OrderVo;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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
    @Autowired
    private OrderRefactorService orderRefactorService;

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
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户id不能为空");
        }
        if (StringUtils.isEmpty(orderDto.getLoadSiteId())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "收货id不能为空");
        }
        if (StringUtils.isEmpty(orderDto.getCustomerContractId())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户合同id不能为空");
        }
        if (this.customerService.getShowCustomerById(orderDto.getCustomerId()) == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户不存在");
        }
        if (this.customerService.getShowCustomerContractById(orderDto.getCustomerContractId()) == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户合同不存在");
        }
        Map<String, Object> result;
        try {
            result = orderService.createOrder(orderDto);
        } catch (Exception ex) {
            return BaseResponse.errorInstance(ex.getMessage());
        }
        return BaseResponse.service(result);
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
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户id不能为空");
        }
        if (this.ordersMapper.selectByPrimaryKey(orderDto.getCustomerId()) == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "订单不存在");
        }
        GetOrderDto getOrderDto;
        try {
            getOrderDto = orderService.updateOrder(orderDto);
        } catch (Exception ex) {
            return BaseResponse.errorInstance(ex.getMessage());
        }
        return BaseResponse.successInstance(getOrderDto);
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
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "订单不存在");
        }
        Map<String, Object> result;
        try {
            result = orderService.deleteOrderById(id);
        } catch (Exception ex) {
            return BaseResponse.errorInstance(ex.getMessage());
        }
        return BaseResponse.service(result);
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
        OrderVo orderVo = new OrderVo();
        try {
            GetOrderDto getOrderDto = orderRefactorService.getOrderById(id);
            if (getOrderDto != null) {
                BeanUtils.copyProperties(getOrderDto, orderVo);

//                CustomerContactsVo contactsVo = new CustomerContactsVo();
//                BeanUtils.copyProperties(getOrderDto.getContactsDto(), contactsVo);
//                orderVo.setContactsDto(contactsVo);

//                BeanUtils.copyProperties(, orderVo.getContactsDto());
//                BeanUtils.copyProperties(getOrderDto.getCreatedUser(), orderVo.getCreatedUser());
//                BeanUtils.copyProperties(getOrderDto.getCustomer(), orderVo.getCustomer());
//                BeanUtils.copyProperties(getOrderDto.getLoadSiteId(), orderVo.getLoadSiteId());
//                BeanUtils.copyProperties(getOrderDto.getModifyUser(), orderVo.getModifyUser());
//                BeanUtils.copyProperties(getOrderDto.getCustomerContract(), orderVo.getCustomerContract());
//                BeanUtils.copyProperties(getOrderDto.getOrderItems(), orderVo.getOrderItems());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), ex.getMessage());
        }
        return BaseResponse.successInstance(orderVo);
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
    @PostMapping("/cancelOrders/{orderId}/{detailInfo}")
    public BaseResponse cancelOrders(@PathVariable("orderId") Integer orderId, @PathVariable("detailInfo") String detailInfo) {
        if (StringUtils.isEmpty(orderId)) {
            return BaseResponse.idNull("订单id不能为空");
        }
        if (StringUtils.isEmpty(detailInfo)) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "取消理由必填");
        }
        return BaseResponse.service(orderService.cancelOrders(orderId, detailInfo));
    }

    /**
     * 待派单列表分页
     *
     * @param criteriaDto
     * @return
     */
    @ApiOperation("待派单列表分页")
    @PostMapping("/listToSendOrders")
    public BaseResponse listToSendOrders(@RequestBody ListOrderCriteriaDto criteriaDto) {
        criteriaDto.setWaitOrders(OrderStatus.PLACE_ORDER);
        return BaseResponse.service(orderService.listOrderByCriteria(criteriaDto));
    }
}
