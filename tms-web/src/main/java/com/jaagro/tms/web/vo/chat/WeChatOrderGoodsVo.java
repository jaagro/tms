package com.jaagro.tms.web.vo.chat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author baiyiran
 * @Date 2018/10/30
 */
@Data
@Accessors(chain = true)
public class WeChatOrderGoodsVo implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer orderItemId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 货物名称
     */
    private String goodsName;

    /**
     * 货物单位：1-羽 2-头 3-吨
     */
    private Integer goodsUnit;

    /**
     * 货物数量
     */
    private Integer goodsQuantity;

    /**
     * 货物重量
     */
    private BigDecimal goodsWeight;

    /**
     * 余量
     */
    private BigDecimal margin;

    /**
     * 是否加药
     */
    private Boolean joinDrug;

}
