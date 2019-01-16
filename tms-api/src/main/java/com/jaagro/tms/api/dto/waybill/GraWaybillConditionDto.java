package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class GraWaybillConditionDto implements Serializable {
    /**
     * 运单Id
     */
    private Integer waybillId;
    /**
     * 司机id
     */
    private Integer driverId;
    /**
     * 抢单状态
     */
    private String status;
}
