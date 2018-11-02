package com.jaagro.tms.web.controller;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.base.GetCustomerUserDto;
import com.jaagro.tms.api.dto.order.*;
import com.jaagro.tms.api.service.OrderRefactorService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.impl.CurrentUserService;
import com.jaagro.tms.web.vo.chat.*;
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
    @Autowired
    private CurrentUserService userService;
    @Autowired
    private CustomerClientService customerClientService;

    /**
     * 查询单条订单
     *
     * @param id
     * @return
     */
    @ApiOperation("查询单条订单")
    @GetMapping("/getWeChatOrderById/{id}")
    public BaseResponse getOrderById(@PathVariable("id") Integer id) {
        WeChatOrderVo chatOrderVo = new WeChatOrderVo();
        try {
            GetOrderDto getOrderDto = orderRefactorService.getOrderById(id);
            if (getOrderDto != null) {
                BeanUtils.copyProperties(getOrderDto, chatOrderVo);
                //客户
                CustomerVo customerVo = new CustomerVo();
                BeanUtils.copyProperties(getOrderDto.getCustomer(), customerVo);
                chatOrderVo.setCustomer(customerVo);
                //装货地
                SiteVo siteVo = new SiteVo();
                BeanUtils.copyProperties(getOrderDto.getLoadSiteId(), siteVo);
                chatOrderVo.setLoadSiteId(siteVo);
                /**
                 * 订单详情Dto转换为Vo
                 */
                List<GetOrderItemsDto> orderItemsDtoList = getOrderDto.getOrderItems();
                List<WeChatOrderItemsVo> orderItemsVoList = new ArrayList<>();
                if (orderItemsDtoList.size() > 0) {
                    for (GetOrderItemsDto orderItemsDto : orderItemsDtoList) {
                        WeChatOrderItemsVo itemsVo = new WeChatOrderItemsVo();
                        BeanUtils.copyProperties(orderItemsDto, itemsVo);
                        //卸货地转换
                        SiteVo vo = new SiteVo();
                        BeanUtils.copyProperties(orderItemsDto.getUnload(), vo);
                        itemsVo.setUnload(vo);
                        orderItemsVoList.add(itemsVo);
                        /**
                         * 订单需求明细Dto转换为Vo
                         */
                        List<GetOrderGoodsDto> goodsDtoList = orderItemsDto.getGoods();
                        List<WeChatOrderGoodsVo> goodsVoList = new ArrayList<>();
                        if (goodsDtoList.size() > 0) {
                            for (GetOrderGoodsDto goodsDto : goodsDtoList) {
                                WeChatOrderGoodsVo goodsVo = new WeChatOrderGoodsVo();
                                BeanUtils.copyProperties(goodsDto, goodsVo);
                                goodsVoList.add(goodsVo);
                            }
                            itemsVo.setGoods(goodsVoList);
                        }
                    }
                    chatOrderVo.setOrderItems(orderItemsVoList);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), ex.getMessage());
        }
        return BaseResponse.successInstance(chatOrderVo);
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
        //获得customer_user中的关联用户id
        GetCustomerUserDto customerUserDto = userService.getCustomerUserById();
        if (customerUserDto != null) {
            criteriaDto.setCustomerId(customerUserDto.getRelevanceId());
            criteriaDto.setCustomerType(customerUserDto.getCustomerType());
        }
        //得到订单分页
        PageInfo pageInfo = orderService.listOrderByCriteria(criteriaDto);
        List<ListOrderDto> orderDtoList = pageInfo.getList();
        List<ListOrderVo> orderVoList = new ArrayList<>();
        //替换为vo
        if (orderDtoList.size() > 0) {
            for (ListOrderDto orderDto : orderDtoList) {
                ListOrderVo orderVo = new ListOrderVo();
                BeanUtils.copyProperties(orderDto, orderVo);
                //循环拷贝Orders下的各个对象
                /*Orders order = ordersMapper.selectByPrimaryKey(orderDto.getId());
                BeanUtils.copyProperties(order, orderDto);
                orderDto
                        .setCustomerId(customerService.getShowCustomerById(order.getCustomerId()))
                        .setCustomerContract(customerService.getShowCustomerContractById(order.getCustomerContractId()))
                        .setLoadSite(customerService.getShowSiteById(order.getLoadSiteId()));
                //归属网点名称
                ShowSiteDto showSiteDto = customerService.getShowSiteById(order.getLoadSiteId());
                orderDto.setDepartmentName(userClientService.getDeptNameById(showSiteDto.getDeptId()));
                //创单人
                UserInfo userInfo = authClientService.getUserInfoById(order.getCreatedUserId(), "employee");
                if (userInfo != null) {
                    ShowUserDto userDto = new ShowUserDto();
                    userDto.setUserName(userInfo.getName());
                    orderDto.setCreatedUserId(userDto);
                }*/
                /**
                 * 替换订单需求Dto为Vo
                 */
                List<ListOrderItemsDto> itemsDtoList = orderDto.getOrderItemsDtoList();
                List<ListOrderItemsVo> itemsVoList = new ArrayList<>();
                if (itemsDtoList.size() > 0) {
                    for (ListOrderItemsDto itemsDto : itemsDtoList) {
                        ListOrderItemsVo itemsVo = new ListOrderItemsVo();
                        BeanUtils.copyProperties(itemsDto, itemsVo);
                        itemsVoList.add(itemsVo);
                        /**
                         * 替换订单需求明细Dto为Vo
                         */
                        List<GetOrderGoodsDto> goodsDtoList = itemsDto.getOrderGoodsDtoList();
                        List<ListOrderGoodsVo> goodsVoList = new ArrayList<>();
                        if (goodsDtoList.size() > 0) {
                            for (GetOrderGoodsDto goodsDto : goodsDtoList) {
                                ListOrderGoodsVo goodsVo = new ListOrderGoodsVo();
                                BeanUtils.copyProperties(goodsDto, goodsVo);
                                goodsVoList.add(goodsVo);
                            }
                            itemsVo.setGoods(goodsVoList);
                        }
                    }
                    orderVo.setItemsVoList(itemsVoList);
                }
                orderVoList.add(orderVo);
            }
        }
        pageInfo.setList(orderVoList);
        return BaseResponse.successInstance(pageInfo);
    }
}
