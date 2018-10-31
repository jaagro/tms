package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.order.GetOrderDto;
import com.jaagro.tms.api.dto.order.GetOrderItemsDto;
import com.jaagro.tms.api.dto.order.ListOrderCriteriaDto;
import com.jaagro.tms.api.service.OrderRefactorService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.web.vo.chat.WeChatOrderItemsVo;
import com.jaagro.tms.web.vo.chat.WeChatOrderVo;
import com.jaagro.tms.web.vo.order.CustomerVo;
import com.jaagro.tms.web.vo.order.SiteVo;
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
                //订单详情
                List<GetOrderItemsDto> orderItemsDtos = getOrderDto.getOrderItems();
                List<WeChatOrderItemsVo> weChatOrderItemsVos = new ArrayList<>();
                if (orderItemsDtos.size() > 0) {
                    for (GetOrderItemsDto orderItemsDto : orderItemsDtos) {
                        WeChatOrderItemsVo itemsVo = new WeChatOrderItemsVo();
                        BeanUtils.copyProperties(orderItemsDto, itemsVo);
                        //卸货地转换
                        SiteVo vo = new SiteVo();
                        BeanUtils.copyProperties(orderItemsDto.getUnload(), vo);
                        itemsVo.setUnload(vo);
                        weChatOrderItemsVos.add(itemsVo);
                    }
                    chatOrderVo.setOrderItems(weChatOrderItemsVos);
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
//        if (StringUtils.isEmpty(criteriaDto.getCustomerId())) {
//            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "客户id不能为空");
//        }
        //暂时数据
        criteriaDto.setCustomerId(244);
        return BaseResponse.service(orderRefactorService.listWeChatOrderByCriteria(criteriaDto));
    }
}
