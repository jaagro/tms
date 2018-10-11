package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.OrderStatus;
import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.api.dto.order.*;
import com.jaagro.tms.api.service.OrderItemsService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.biz.entity.OrderItems;
import com.jaagro.tms.biz.entity.OrderModifyLog;
import com.jaagro.tms.biz.entity.Orders;
import com.jaagro.tms.biz.entity.Waybill;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.AuthClientService;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.UserClientService;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author tony
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CurrentUserService currentUserService;
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
    private OrderModifyLogMapper modifyLogMapper;
    @Autowired
    private WaybillMapperExt waybillMapper;
    @Autowired
    private UserClientService userClientService;

    /**
     * 创建订单
     *
     * @param orderDto 入参json
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createOrder(CreateOrderDto orderDto) {
        Orders order = new Orders();
        BeanUtils.copyProperties(orderDto, order);
        order.setCreatedUserId(currentUserService.getShowUser().getId());
        order.setDepartmentId(currentUserService.getCurrentUser().getDepartmentId());
        this.ordersMapper.insertSelective(order);
        if (orderDto.getOrderItems() != null && orderDto.getOrderItems().size() > 0) {
            for (CreateOrderItemsDto itemsDto : orderDto.getOrderItems()) {
                if (StringUtils.isEmpty(itemsDto.getUnloadId())) {
                    return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "卸货地不能为空");
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
     * 修改订单
     *
     * @param orderDto 入参json
     * @return 修改后的order对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public GetOrderDto updateOrder(UpdateOrderDto orderDto) {
        if (orderDto.getId() == null) {
            throw new NullPointerException("订单id不能为空");
        }
        if (ordersMapper.selectByPrimaryKey(orderDto.getId()) == null) {
            throw new NullPointerException("订单查询无数据");
        }
        //修改order
        Orders orders = new Orders();
        BeanUtils.copyProperties(orderDto, orders);
        orders
                .setModifyTime(new Date())
                .setModifyUserId(this.currentUserService.getShowUser().getId());
        this.ordersMapper.updateByPrimaryKeySelective(orders);

        if (orderDto.getOrderItems() != null && orderDto.getOrderItems().size() > 0) {
            for (CreateOrderItemsDto itemsDto : orderDto.getOrderItems()) {
                this.orderItemsService.updateItems(itemsDto);
            }
        }

        //返回
        Orders order = this.ordersMapper.selectByPrimaryKey(orderDto.getId());
        GetOrderDto getOrderDto = new GetOrderDto();
        BeanUtils.copyProperties(order, orderDto);
        getOrderDto
                .setCustomer(this.customerService.getShowCustomerById(order.getCustomerId()))
                .setCustomerContract(this.customerService.getShowCustomerContractById(order.getCustomerContractId()))
                .setLoadSiteId(this.customerService.getShowSiteById(order.getLoadSiteId()));
        UserInfo userInfo = this.authClientService.getUserInfoById(order.getCreatedUserId(), "employee");
        if (userInfo != null) {
            ShowUserDto userDto = new ShowUserDto();
            userDto.setUserName(userInfo.getName());
            getOrderDto.setCreatedUser(userDto);
        }
        return getOrderDto;
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
        if(null == order){
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
     * 分页获取订单列表
     *
     * @param criteriaDto 查询条件 json
     * @return 订单列表
     */
    @Override
    public Map<String, Object> listOrderByCriteria(ListOrderCriteriaDto criteriaDto) {
        PageHelper.startPage(criteriaDto.getPageNum(), criteriaDto.getPageSize());
        Set<Integer>  departIds = userClientService.getDownDepartment();
        List<Integer> dids = new ArrayList<>(departIds);
        if (dids.size()!=0){
            criteriaDto.setDepartIds(dids);
        }
        List<ListOrderDto> orderDtos = this.ordersMapper.listOrdersByCriteria(criteriaDto);
        if (orderDtos != null && orderDtos.size() > 0) {
            for (ListOrderDto orderDto : orderDtos
            ) {
                Orders order = this.ordersMapper.selectByPrimaryKey(orderDto.getId());
                BeanUtils.copyProperties(order, orderDto);
                orderDto
                        .setCustomerId(this.customerService.getShowCustomerById(order.getCustomerId()))
                        .setCustomerContract(this.customerService.getShowCustomerContractById(order.getCustomerContractId()))
                        .setLoadSite(this.customerService.getShowSiteById(order.getLoadSiteId()));
                UserInfo userInfo = this.authClientService.getUserInfoById(order.getCreatedUserId(), "employee");
                if (userInfo != null) {
                    ShowUserDto userDto = new ShowUserDto();
                    userDto.setUserName(userInfo.getName());
                    orderDto.setCreatedUserId(userDto);
                }
                //派单进度
                List<Waybill> waybills = waybillMapper.listWaybillByOrderId(orderDto.getId());
                if (waybills.size() > 0) {
                    orderDto.setWaybillCount(waybills.size());
                    //已派单
                    List<Waybill> waitWaybills = waybillMapper.listWaybillWaitByOrderId(orderDto.getId());
                    if (waitWaybills.size() > 0) {
                        orderDto.setWaybillAlready(waitWaybills.size());
                        orderDto.setWaybillWait(orderDto.getWaybillCount() - orderDto.getWaybillAlready());
                    }
                }

            }
        }
        return ServiceResult.toResult(new PageInfo<>(orderDtos));
    }

    /**
     * 【逻辑】删除订单
     *
     * @param id 订单id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> deleteOrderById(Integer id) {
        Orders orders = this.ordersMapper.selectByPrimaryKey(id);
        if (orders == null) {
            throw new NullPointerException("订单不存在");
        }
        orders.setOrderStatus(OrderStatus.CANCEL);
        ordersMapper.updateByPrimaryKeySelective(orders);
        List<OrderItems> orderItems = this.orderItemsMapper.listByOrderId(orders.getId());
        if (orderItems != null) {
            this.orderItemsService.disableByOrderId(id);
        }
        return ServiceResult.toResult("删除成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> cancelOrders(Integer orderId, String detailInfo) {
        Orders orders = this.ordersMapper.selectByPrimaryKey(orderId);
        if (orders == null) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "订单不存在");
        }
        // 修改日志
        OrderModifyLog modifyLog = new OrderModifyLog();
        modifyLog
                .setOrderId(orderId)
                .setCreatedUserId(this.currentUserService.getCurrentUser().getId())
                .setNewInfo(detailInfo);
        this.modifyLogMapper.insertSelective(modifyLog);
        // 订单
        orders
                .setOrderStatus(OrderStatus.CANCEL)
                .setModifyUserId(this.currentUserService.getCurrentUser().getId())
                .setModifyTime(new Date());
        this.ordersMapper.updateByPrimaryKeySelective(orders);
        // 订单明细
        List<OrderItems> orderItems = this.orderItemsMapper.listByOrderId(orders.getId());
        if (orderItems != null) {
            this.orderItemsService.disableByOrderId(orderId);
        }
        return ServiceResult.toResult("取消订单成功");
    }
}
