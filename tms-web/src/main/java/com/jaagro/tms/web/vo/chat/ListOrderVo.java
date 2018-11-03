package com.jaagro.tms.web.vo.chat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author baiyiran
 * @Date 2018/10/22
 */
@Data
@Accessors(chain = true)
public class ListOrderVo implements Serializable {

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
    private SiteVo loadSiteId;

    /**
     * 要求装货时间
     */
    private Date loadTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 订单列表
     */
    private List<ListOrderItemsVo> itemsVoList;

}
