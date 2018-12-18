package com.jaagro.tms.api.dto.truck;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author baiyiran
 * @since 2018/12/18
 */
@Data
@Accessors(chain = true)
public class ChangeTruckDto implements Serializable {
    /**
     * 车辆ID
     */
    private Integer id;

    /**
     * 关联车队表ID
     */
    private Integer truckTeamId;

    /**
     * 车牌号码
     */
    private String truckNumber;

    /**
     * 当前司机数
     */
    private Integer driverAmount;
}
