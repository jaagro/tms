package com.jaagro.tms.web.vo.chat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author baiyiran
 * @Date 2018/10/30
 */
@Data
@Accessors(chain = true)
public class WeChatOrderVo implements Serializable {

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
     * 客户id
     */
    private CustomerVo customer;

    /**
     * 装货地id
     */
    private SiteVo loadSiteId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 订单列表
     */
    private List<WeChatOrderItemsVo> orderItems;

    /**
     * 运单列表
     */
    private List<ListWaybillVo> waybillVoList;

}
