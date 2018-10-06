package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class GetWaybillGoodsDto implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     *
     */
    private Integer waybillItemId;

    /**
     * 货物名称
     */
    private String goodsName;

    /**
     * 货物单位：1-羽 2-头 3-吨
     */
    private Integer goodsUnit;

    /**
     * 计划数量
     */
    private Integer goodsQuantity;

    /**
     * 计划重量
     */
    private BigDecimal goodsWeight;

    /**
     * 装货数量
     */
    private Integer loadQuantity;

    /**
     * 装货重量
     */
    private BigDecimal loadWeight;

    /**
     * 卸货数量
     */
    private Integer unloadQuantity;

    /**
     * 卸货重量
     */
    private BigDecimal unloadWeight;

    /**
     * 是否加药
     */
    private Boolean joinDrug;

    /**
     * 订单货物id
     */
    private Integer orderGoodsId;
}
