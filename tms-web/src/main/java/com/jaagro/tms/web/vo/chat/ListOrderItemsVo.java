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
public class ListOrderItemsVo implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 卸货地id
     */
    private SiteVo unload;

    /**
     * 要求卸货时间
     */
    private Date unloadTime;

    /**
     * 货物列表
     */
    private List<ListOrderGoodsVo> goods;

}
