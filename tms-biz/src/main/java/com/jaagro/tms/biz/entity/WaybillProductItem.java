package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class WaybillProductItem implements Serializable {
    private Long waybillProductItemId;

    private Long waybillProductId;

    private String productName;

    private Integer quantity;

    private BigDecimal weight;

    private BigDecimal chargeableWeight;

    private Boolean appliedMedicine;

    private String loadingPosition;
}