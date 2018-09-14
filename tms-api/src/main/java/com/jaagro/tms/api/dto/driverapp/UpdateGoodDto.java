package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author @Gao.
 */

/**
 * 确认提货信息后,更新货物字段
 */
@Data
@Accessors(chain = true)
public class UpdateGoodDto implements Serializable {
    /**
     * 货物id
     */
    private Integer waybillGoodId;

    /**
     * 装货重量
     */
    private BigDecimal loadWeight;

}
