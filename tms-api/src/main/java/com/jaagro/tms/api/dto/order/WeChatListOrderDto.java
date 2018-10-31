package com.jaagro.tms.api.dto.order;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author baiyiran
 */
@Data
@Accessors(chain = true)
public class WeChatListOrderDto implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 货物类型
     */
    private Integer goodsType;

    /**
     * 订单状态:
     */
    private String orderStatus;

    /**
     * 装货地id
     */
    private WeChatOrderSiteDto loadSite;

    /**
     * 要求装货时间
     */
    private Date loadTime;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 是否需要纸质回单
     */
    private Boolean paperReceipt;

    /**
     * 订单详情
     */
    private List<WeChatListOrderItemsDto> orderItemsDtos;

}
