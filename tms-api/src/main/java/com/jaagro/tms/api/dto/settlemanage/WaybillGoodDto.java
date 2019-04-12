package com.jaagro.tms.api.dto.settlemanage;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 运单商品信息
 * @author: @Gao.
 * @create: 2019-04-11 16:22
 **/
@Data
@Accessors(chain = true)
public class WaybillGoodDto implements Serializable {
    /**
     * 货物名称
     */
    private String goodsName;

    /**
     * 货物单位：1-羽 2-头 3-吨
     */
    private Integer goodsUnit;

    /**
     * 卸货数量
     */
    private Integer unloadQuantity;

    /**
     * 卸货重量
     */
    private BigDecimal unloadWeight;
}
