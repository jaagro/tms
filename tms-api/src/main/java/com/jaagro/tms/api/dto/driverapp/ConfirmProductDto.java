package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class ConfirmProductDto implements Serializable {

    /**
     * waybillItemId
     */
    private Integer waybillItemId;
    /**
     * 卸货地 id
     */
    private Integer unLoadSiteId;
    /**
     * 运单货物id
     */
    private Integer waybillGoodId;

    /**
     * 装货重量
     */
    private BigDecimal loadWeight;

    /**
     * 卸货重量
     */
    private BigDecimal unloadWeight;

}
