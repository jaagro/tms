package com.jaagro.tms.api.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liqiangping
 */
@Data
@Accessors(chain = true)
public class TruckLoadTempBo implements Serializable {

    /**
     * 车型id
     */
    private Integer id;

    /**
     * 装货类型
     */
    private String productName;

    /**
     * 最大装载重量
     */
    private BigDecimal maxWeight;

    /**
     * 最大装载数量
     */
    private Integer maxAmount;

    /**
     * 已装重量
     */
    private BigDecimal loadWeight;

    /**
     * 已装数量
     */
    private Integer loadAmount;
}
