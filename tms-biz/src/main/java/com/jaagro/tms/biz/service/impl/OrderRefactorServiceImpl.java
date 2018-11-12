package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.order.*;
import com.jaagro.tms.api.service.OrderItemsService;
import com.jaagro.tms.api.service.OrderRefactorService;
import com.jaagro.tms.biz.entity.Orders;
import com.jaagro.tms.biz.mapper.OrderGoodsMapperExt;
import com.jaagro.tms.biz.mapper.OrderItemsMapperExt;
import com.jaagro.tms.biz.mapper.OrdersMapperExt;
import com.jaagro.tms.biz.service.AuthClientService;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author baiyiran
 */
@Service
public class OrderRefactorServiceImpl implements OrderRefactorService {

    private static final Logger log = LoggerFactory.getLogger(OrderRefactorServiceImpl.class);

    @Autowired
    private OrdersMapperExt ordersMapper;
    @Autowired
    private CustomerClientService customerService;
    @Autowired
    private OrderItemsService orderItemsService;
    @Autowired
    private OrderItemsMapperExt orderItemsMapper;
    @Autowired
    private OrderGoodsMapperExt orderGoodsMapper;
    @Autowired
    private AuthClientService authClientService;
    @Autowired
    private CurrentUserService currentUserService;

    /**
     * 创建订单
     *
     * @param orderDto 入参json
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createOrder(CreateOrderDto orderDto) {
        if (customerService.getShowCustomerById(orderDto.getCustomerId()) == null) {
            throw new RuntimeException("下单客户不存在");
        }
        if (customerService.getShowCustomerContractById(orderDto.getCustomerContractId()) == null) {
            throw new RuntimeException("下单客户合同不存在");
        }
        Orders order = new Orders();
        BeanUtils.copyProperties(orderDto, order);
        order.setCreatedUserId(currentUserService.getShowUser().getId());
        //暂时将departmentId定为1，仅限小程序客户下单
        order.setDepartmentId(1);
        this.ordersMapper.insertSelective(order);
        if (orderDto.getOrderItems() != null && orderDto.getOrderItems().size() > 0) {
            for (CreateOrderItemsDto itemsDto : orderDto.getOrderItems()) {
                if (StringUtils.isEmpty(itemsDto.getUnloadId())) {
                    throw new RuntimeException("卸货地址不能为空");
                }
                itemsDto.setOrderId(order.getId());
                this.orderItemsService.createOrderItem(itemsDto);
            }
        } else {
            throw new RuntimeException("订单明细不能为空");
        }
        return ServiceResult.toResult("创建成功");
    }

    /**
     * 获取单条订单
     *
     * @param id 订单id
     * @return order对象
     */
    @Override
    public GetOrderDto getOrderById(Integer id) {
        Orders order = this.ordersMapper.selectByPrimaryKey(id);
        if (null == order) {
            throw new NullPointerException(id + " 订单不存在");
        }
        GetOrderDto orderDto = new GetOrderDto();
        BeanUtils.copyProperties(order, orderDto);
        orderDto
                .setContactsDto(this.customerService.getCustomerContactByCustomerId(order.getCustomerId()))
                .setCustomer(this.customerService.getShowCustomerById(order.getCustomerId()))
                .setCustomerContract(this.customerService.getShowCustomerContractById(order.getCustomerContractId()))
                .setLoadSiteId(this.customerService.getShowSiteById(order.getLoadSiteId()))
                .setOrderItems(this.orderItemsService.listByOrderId(order.getId()));
        UserInfo userInfo = this.authClientService.getUserInfoById(order.getCreatedUserId(), "employee");
        if (userInfo != null) {
            ShowUserDto userDto = new ShowUserDto();
            userDto.setUserName(userInfo.getName());
            orderDto.setCreatedUser(userDto);
        }
        return orderDto;
    }

    /**
     * 微信小程序 分页获取订单列表
     *
     * @param criteriaDto 查询条件 json
     * @return 订单列表
     */
    @Override
    public Map<String, Object> listWeChatOrderByCriteria(ListOrderCriteriaDto criteriaDto) {
        PageHelper.startPage(criteriaDto.getPageNum(), criteriaDto.getPageSize());
        if (criteriaDto.getCustomerId() == null) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "获取当前用户失败");
        }
        if (criteriaDto.getCustomerType() == null) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "获取当前用户类型失败");
        }
        List<WeChatListOrderDto> orderDtos = this.ordersMapper.listWeChatOrdersByCriteria(criteriaDto);
        if (orderDtos != null && orderDtos.size() > 0) {
            for (WeChatListOrderDto listOrderDto : orderDtos) {
                Orders order = this.ordersMapper.selectByPrimaryKey(listOrderDto.getId());
                BeanUtils.copyProperties(order, listOrderDto);
                ShowSiteDto showSiteDto = this.customerService.getShowSiteById(order.getLoadSiteId());
                WeChatOrderSiteDto chatOrderSiteDto = new WeChatOrderSiteDto();
                BeanUtils.copyProperties(showSiteDto, chatOrderSiteDto);
                listOrderDto.setLoadSite(chatOrderSiteDto);
                //订单需求
                List<GetOrderItemsDto> orderItemsDtos = this.orderItemsService.listByOrderId(listOrderDto.getId());
                if (orderItemsDtos.size() > 0) {
                    List<WeChatListOrderItemsDto> weChatListOrderItemsDtos = new ArrayList<>();
                    for (GetOrderItemsDto orderItemsDto : orderItemsDtos) {
                        WeChatListOrderItemsDto itemsDto = new WeChatListOrderItemsDto();
                        BeanUtils.copyProperties(orderItemsDto, itemsDto);
                        //订单需求的卸货地址转换
                        WeChatOrderSiteDto weChatOrderSiteDto = new WeChatOrderSiteDto();
                        BeanUtils.copyProperties(orderItemsDto.getUnload(), weChatOrderSiteDto);
                        itemsDto.setUnload(weChatOrderSiteDto);
                        weChatListOrderItemsDtos.add(itemsDto);
                    }
                    listOrderDto.setOrderItemsDtos(weChatListOrderItemsDtos);
                }
            }
        }
        return ServiceResult.toResult(new PageInfo<>(orderDtos));
    }
}
