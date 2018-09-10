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
public class ShowGoodsDto implements Serializable {
    /**
     *
     */
    private Integer id;

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
     * 是否加药
     */
    private Boolean joinDrug;

    /**
     * 装货重量
     */
    private BigDecimal loadWeight;

    /**
     * 卸货重量
     */
    private Integer unloadWeight;
}
