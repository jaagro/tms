package com.jaagro.tms.web.controller;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.constant.OrderStatus;
import com.jaagro.tms.api.dto.order.*;
import com.jaagro.tms.api.dto.waybill.ListWaybillDto;
import com.jaagro.tms.api.service.OrderRefactorService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.UserClientService;
import com.jaagro.tms.web.vo.chat.*;
import com.jaagro.tms.web.vo.pc.ListOrderVo;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private OrderRefactorService orderRefactorService;
    @Autowired
    private UserClientService userClientService;
    @Autowired
    private WaybillService waybillService;

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
        GetOrderDto getOrderDto;
        try {
            getOrderDto = orderService.updateOrder(orderDto);
        } catch (Exception ex) {
            ex.printStackTrace();
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
        Map<String, Object> result;
        try {
            result = orderService.deleteOrderById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
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
    @PostMapping("/listOrders")
    public BaseResponse listOrders(@RequestBody ListOrderCriteriaDto criteriaDto) {
        if (StringUtils.isEmpty(criteriaDto.getPageNum())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageNum不能为空");
        }
        if (StringUtils.isEmpty(criteriaDto.getPageSize())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageSize不能为空");
        }
        List<Integer> departIds = userClientService.getDownDepartment();
        List<Integer> dids = new ArrayList<>(departIds);
        if (dids.size() != 0) {
            criteriaDto.setDepartIds(dids);
        }
        List<ListOrderDto> orderDtoList = orderService.listOrderByCriteria(criteriaDto);
        List<ListOrderVo> orderVoList = new ArrayList<>();
        if (orderDtoList.size() > 0) {
            BeanUtils.copyProperties(orderDtoList, orderVoList);
            for (ListOrderVo orderVo : orderVoList) {
                //循环拷贝Orders下的各个对象
                /*Orders order = this.ordersMapper.selectByPrimaryKey(orderDto.getId());
                BeanUtils.copyProperties(order, orderDto);
                orderDto
                        .setCustomerId(this.customerService.getShowCustomerById(order.getCustomerId()))
                        .setCustomerContract(this.customerService.getShowCustomerContractById(order.getCustomerContractId()))
                        .setLoadSite(this.customerService.getShowSiteById(order.getLoadSiteId()));
                //归属网点名称
                ShowSiteDto showSiteDto = this.customerService.getShowSiteById(order.getLoadSiteId());
                orderDto.setDepartmentName(this.userClientService.getDeptNameById(showSiteDto.getDeptId()));
                //创单人
                UserInfo userInfo = this.authClientService.getUserInfoById(order.getCreatedUserId(), "employee");
                if (userInfo != null) {
                    ShowUserDto userDto = new ShowUserDto();
                    userDto.setUserName(userInfo.getName());
                    orderDto.setCreatedUserId(userDto);
                }*/
                //派单进度
                List<ListWaybillDto> waybills = waybillService.listWaybillByOrderId(orderVo.getId());
                if (waybills.size() > 0) {
                    orderVo.setWaybillCount(waybills.size());
                    //已派单
                    List<ListWaybillDto> waitWaybills = waybillService.listWaybillWaitByOrderId(orderVo.getId());
                    if (waitWaybills.size() > 0) {
                        orderVo.setWaybillAlready(waitWaybills.size());
                        orderVo.setWaybillWait(orderVo.getWaybillCount() - orderVo.getWaybillAlready());
                    }
                }
            }
        }
        return BaseResponse.successInstance(new PageInfo<>(orderVoList));
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
        //区分订单列表和待派单列表
        criteriaDto.setWaitOrders(OrderStatus.PLACE_ORDER);
        //部门隔离
        List<Integer> departIds = userClientService.getDownDepartment();
        List<Integer> dids = new ArrayList<>(departIds);
        if (dids.size() != 0) {
            criteriaDto.setDepartIds(dids);
        }
        List<ListOrderDto> orderDtoList = orderService.listOrderByCriteria(criteriaDto);
        List<ListOrderVo> orderVoList = new ArrayList<>();
        if (orderDtoList.size() > 0) {
            BeanUtils.copyProperties(orderDtoList, orderVoList);
            for (ListOrderVo orderVo : orderVoList) {
                //循环拷贝Orders下的各个对象
                /*Orders order = this.ordersMapper.selectByPrimaryKey(orderDto.getId());
                BeanUtils.copyProperties(order, orderDto);
                orderDto
                        .setCustomerId(this.customerService.getShowCustomerById(order.getCustomerId()))
                        .setCustomerContract(this.customerService.getShowCustomerContractById(order.getCustomerContractId()))
                        .setLoadSite(this.customerService.getShowSiteById(order.getLoadSiteId()));
                //归属网点名称
                ShowSiteDto showSiteDto = this.customerService.getShowSiteById(order.getLoadSiteId());
                orderDto.setDepartmentName(this.userClientService.getDeptNameById(showSiteDto.getDeptId()));
                //创单人
                UserInfo userInfo = this.authClientService.getUserInfoById(order.getCreatedUserId(), "employee");
                if (userInfo != null) {
                    ShowUserDto userDto = new ShowUserDto();
                    userDto.setUserName(userInfo.getName());
                    orderDto.setCreatedUserId(userDto);
                }*/
            }
        }
        return BaseResponse.successInstance(new PageInfo<>(orderVoList));
    }
}
