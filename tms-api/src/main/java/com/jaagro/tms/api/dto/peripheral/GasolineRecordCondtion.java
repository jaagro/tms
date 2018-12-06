package com.jaagro.tms.api.dto.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class GasolineRecordCondtion implements Serializable {
    /**
     * 司机id
     */
    private Integer driverId;
}
