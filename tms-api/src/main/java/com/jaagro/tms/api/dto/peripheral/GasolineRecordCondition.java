package com.jaagro.tms.api.dto.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class GasolineRecordCondition implements Serializable {
    /**
     *
     */
    private Integer id;
    /**
     * 司机id
     */
    private Integer driverId;
    /**
     * 车牌号码
     */
    private String truckNumber;
}
