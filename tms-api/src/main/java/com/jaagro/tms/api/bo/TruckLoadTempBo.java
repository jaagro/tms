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
     * 最大装载量
     */
    private BigDecimal maxQuantity;

    /**
     * 已装量
     */
    private BigDecimal loadQuantity;
}
