package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author baiyiran
 * @Date 2018/12/18
 */
@Accessors
@Data
public class TransferTruckDto implements Serializable {

    /**
     * 要换的车辆id
     */
    public Integer truckId;

    /**
     * 司机id
     */
    public Integer driverId;


}
