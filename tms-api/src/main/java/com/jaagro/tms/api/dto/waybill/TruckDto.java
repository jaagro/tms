package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TruckDto {

    /**
     *需要车型Id
     */
    private Integer truckId;
    /**
     *车子承载量，单位是吨
     */
    private Integer capacity;
    /**
     * 需要车子数量
     */
    private Integer number;
}
