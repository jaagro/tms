package com.jaagro.tms.web.controller;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.constant.CustomerType;
import com.jaagro.tms.api.dto.base.GetCustomerUserDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.order.*;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.OrderRefactorService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.impl.CurrentUserService;
import com.jaagro.tms.web.vo.chat.*;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    @Autowired
    private WaybillService waybillService;

    /**
     * 新增订单
     *
     * @param orderDto
     * @return
     */
    @ApiOperation("新增订单")
    @PostMapping("/weChatOrder")
    public BaseResponse createOrder(@RequestBody CreateOrderDto orderDto) {
        if (StringUtils.isEmpty(orderDto.getLoadSiteId())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "装货地不能为空");
        }
        if (StringUtils.isEmpty(orderDto.getCustomerContractId())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户合同不能为空");
        }
        //获取当前用户
        GetCustomerUserDto customerUserDto = userService.getCustomerUserById();
        if (customerUserDto == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "当前客户获取失败");
        }
        //客户
        if (customerUserDto.getCustomerType().equals(CustomerType.CUSTOER)) {
            orderDto.setCustomerId(customerUserDto.getRelevanceId());
        } else {
            //装货地
            //通过装卸货地获得客户id
            ShowSiteDto showSiteDto = customerClientService.getShowSiteById(customerUserDto.getRelevanceId());
            if (showSiteDto == null) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "当前客户获取失败");
            }
            if (showSiteDto.getCustomerId() == null) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "当前客户获取失败");
            }
            orderDto.setCustomerId(showSiteDto.getCustomerId());
        }
        Map<String, Object> result;
        try {
            result = orderRefactorService.createOrder(orderDto);
        } catch (Exception ex) {
            log.error("微信小程序开单:" + ex.getMessage());
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
     * 根据订单id查询运单分页
     *
     * @param criteriaDto
     * @return
     */
    @ApiOperation("根据订单id查询运单分页")
    @PostMapping("/listWaybillByCriteriaForWeChat")
    public BaseResponse listWaybillByCriteriaForWeChat(@RequestBody ListWaybillCriteriaDto criteriaDto) {
        if (StringUtils.isEmpty(criteriaDto.getPageNum())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageNum不能为空");
        }
        if (StringUtils.isEmpty(criteriaDto.getPageSize())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageSize不能为空");
        }
        if (StringUtils.isEmpty(criteriaDto.getOrderId())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "订单id不能为空");
        }
        //运单列表
        PageInfo pageInfo = waybillService.listWaybillByCriteriaForWechat(criteriaDto);
        List<GetWaybillDto> getWaybillDtos = pageInfo.getList();
        if (getWaybillDtos.size() > 0) {
            List<ListWaybillVo> waybillVoList = new ArrayList<>();
            //运单
            for (GetWaybillDto waybillDto : getWaybillDtos) {
                ListWaybillVo waybillVo = new ListWaybillVo();
                BeanUtils.copyProperties(waybillDto, waybillVo);
                //运单装货地
                if (waybillDto.getLoadSite() != null) {
                    ShowSiteVo showSiteVo = new ShowSiteVo();
                    BeanUtils.copyProperties(waybillDto.getLoadSite(), showSiteVo);
                    waybillVo.setLoadSiteVo(showSiteVo);
                }
                List<ListWaybillItemsVo> listWaybillItemsVoList = new ArrayList<>();
                //运单详细
                if (waybillDto.getWaybillItems().size() > 0) {
                    for (GetWaybillItemDto itemDto : waybillDto.getWaybillItems()) {
                        ListWaybillItemsVo itemsVo = new ListWaybillItemsVo();
                        BeanUtils.copyProperties(itemDto, itemsVo);
                        //运单详情的卸货地址
                        if (itemDto.getShowSiteDto() != null) {
                            ShowSiteVo showSiteVo = new ShowSiteVo();
                            BeanUtils.copyProperties(itemDto.getShowSiteDto(), showSiteVo);
                            itemsVo.setUploadSiteVo(showSiteVo);
                        }
                        listWaybillItemsVoList.add(itemsVo);
                    }
                }
                waybillVo.setWaybillItemsVoList(listWaybillItemsVoList);
                waybillVoList.add(waybillVo);
            }
            pageInfo.setList(waybillVoList);
        }
        return BaseResponse.successInstance(pageInfo);

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
        criteriaDto.setDifferentiateStatus("WeChat");
        PageInfo pageInfo = orderService.listOrderByCriteria(criteriaDto);
        List<ListOrderDto> orderDtoList = pageInfo.getList();
        List<ListOrderVo> orderVoList = new ArrayList<>();
        //替换为vo
        if (orderDtoList.size() > 0) {
            for (ListOrderDto orderDto : orderDtoList) {
                ListOrderVo orderVo = new ListOrderVo();
                BeanUtils.copyProperties(orderDto, orderVo);
                //装货地
                ShowSiteDto showSiteDto = customerClientService.getShowSiteById(orderDto.getLoadSiteId());
                SiteVo siteVo = new SiteVo();
                BeanUtils.copyProperties(showSiteDto, siteVo);
                orderVo.setLoadSiteId(siteVo);
                /**
                 * 替换订单需求Dto为Vos
                 */
                List<ListOrderItemsDto> itemsDtoList = orderDto.getOrderItemsDtoList();
                List<ListOrderItemsVo> itemsVoList = new ArrayList<>();
                if (itemsDtoList.size() > 0) {
                    for (ListOrderItemsDto itemsDto : itemsDtoList) {
                        ListOrderItemsVo itemsVo = new ListOrderItemsVo();
                        BeanUtils.copyProperties(itemsDto, itemsVo);
                        ShowSiteDto siteDto = customerClientService.getShowSiteById(itemsDto.getUnloadId());
                        SiteVo vo = new SiteVo();
                        BeanUtils.copyProperties(siteDto, vo);
                        itemsVo.setUnload(vo);
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

    /**
     * 我的—获取当前登录用户的信息
     *
     * @param criteriaDto
     * @return
     */
    @ApiOperation("小程序-我的")
    @PostMapping("/getCurrentUserPhone")
    public BaseResponse getCurrentUserPhone() {
        //获得customer_user中的关联用户id
        GetCustomerUserDto customerUserDto = userService.getCustomerUserById();
        if (customerUserDto != null) {
            MyInfoVo myInfoVo = new MyInfoVo();
            if (customerUserDto.getCustomerType().equals(CustomerType.CUSTOER)) {
                ShowCustomerDto customerDto = customerClientService.getShowCustomerById(customerUserDto.getRelevanceId());
                if (customerDto != null) {
                    myInfoVo
                            .setCustomerId(customerDto.getId())
                            .setName(customerDto.getCustomerName())
                            .setPhone(customerUserDto.getPhoneNumber());
                }
            } else {
                ShowSiteDto showSiteDto = customerClientService.getShowSiteById(customerUserDto.getRelevanceId());
                if (showSiteDto != null) {
                    myInfoVo
                            .setCustomerId(showSiteDto.getCustomerId())
                            .setName(showSiteDto.getSiteName())
                            .setPhone(customerUserDto.getPhoneNumber());
                }
            }
            return BaseResponse.successInstance(myInfoVo);
        }
        return BaseResponse.successInstance(null);
    }
}
