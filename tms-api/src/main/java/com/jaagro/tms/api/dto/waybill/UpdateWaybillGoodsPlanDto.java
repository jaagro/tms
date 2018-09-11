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
public class UpdateWaybillGoodsPlanDto implements Serializable {

    /**
     * 货物id
     */
    private Integer goodsId;

    /**
     * 余量
     */
    private BigDecimal margin;
}
