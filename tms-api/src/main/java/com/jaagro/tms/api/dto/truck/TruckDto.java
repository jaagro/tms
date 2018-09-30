package com.jaagro.tms.api.dto.truck;

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
    /**
     * 在途时间：单位分钟
     */
    private int travelTime;
    /**
     * 里程数：单位公里
     */
    private int kilometres;
}
