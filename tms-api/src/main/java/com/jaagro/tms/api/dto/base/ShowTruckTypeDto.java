package com.jaagro.tms.api.dto.base;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ShowTruckTypeDto implements Serializable {

    /**
     * 车型表主键id
     */
    private Integer id;

    /**
     * 车型名称
     */
    private String typeName;

    /**
     * 货物类型名称
     */
    private String productName;

    /**
     * 车长
     */
    private String truckLength;

    /**
     * 车辆载重
     */
    private String truckWeight;

    /**
     * 承载总数
     */
    private String truckAmount;
}
