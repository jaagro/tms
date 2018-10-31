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
public class WeChatListOrderItemsDto implements Serializable {

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
    private WeChatOrderSiteDto unload;

    /**
     * 要求卸货时间
     */
    private Date unloadTime;

    /**
     * 是否有效
     */
    private Boolean enabled;

    /**
     * 货物列表
     */
    private List<WeChatListOrderGoodsDto> goods;
}
