package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.order.GetOrderDto;
import com.jaagro.tms.api.dto.order.ListOrderCriteriaDto;
import com.jaagro.tms.api.service.OrderRefactorService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.web.vo.order.*;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private OrderRefactorService orderRefactorService;

    /**
     * 查询单条订单
     *
     * @param id
     * @return
     */
    @ApiOperation("查询单条订单")
    @GetMapping("/getWeChatOrderById/{id}")
    public BaseResponse getOrderById(@PathVariable("id") Integer id) {
        OrderVo orderVo = new OrderVo();
        try {
            GetOrderDto getOrderDto = orderRefactorService.getOrderById(id);
            if (getOrderDto != null) {
                BeanUtils.copyProperties(getOrderDto, orderVo);
                //联系人
                CustomerContactsVo contactsVo = new CustomerContactsVo();
                BeanUtils.copyProperties(getOrderDto.getContactsDto(), contactsVo);
                orderVo.setContactsDto(contactsVo);
                //创建人
                UserVo userVo = new UserVo();
                BeanUtils.copyProperties(getOrderDto.getCreatedUser(), userVo);
                orderVo.setCreatedUser(userVo);
                //客户
                CustomerVo customerVo = new CustomerVo();
                BeanUtils.copyProperties(getOrderDto.getCustomer(), customerVo);
                orderVo.setCustomer(customerVo);
                //装货地
                SiteVo siteVo = new SiteVo();
                BeanUtils.copyProperties(getOrderDto.getLoadSiteId(), siteVo);
                orderVo.setLoadSiteId(siteVo);
                //修改人
                if (getOrderDto.getModifyUser() != null) {
                    UserVo userModifyVo = new UserVo();
                    BeanUtils.copyProperties(getOrderDto.getModifyUser(), userModifyVo);
                    orderVo.setModifyUser(userModifyVo);
                }
                //客户合同
                ShowCustomerContractVo showCustomerContractVo = new ShowCustomerContractVo();
                BeanUtils.copyProperties(getOrderDto.getCustomerContract(), showCustomerContractVo);
                orderVo.setCustomerContract(showCustomerContractVo);
                //订单详情
                BeanUtils.copyProperties(getOrderDto.getOrderItems(), orderVo.getOrderItems());
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
    @PostMapping("/listWeChatOrders")
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
    @PostMapping("/cancelWeChatOrder/{orderId}/{detailInfo}")
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
