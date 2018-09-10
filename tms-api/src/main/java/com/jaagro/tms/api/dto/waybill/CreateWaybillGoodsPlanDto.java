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
public class CreateWaybillGoodsPlanDto implements Serializable {

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 待配载余量
     */
    private BigDecimal surplus;
}
