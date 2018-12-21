package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author baiyiran
 */
@Data
@Accessors(chain = true)
public class UpdateDriverDto implements Serializable {
    /**
     * 司机id
     */
    private Integer id;

    /**
     * 关联车队表ID
     */
    private Integer truckTeamId;

    /**
     * 关联车辆ID
     */
    private Integer truckId;

}
