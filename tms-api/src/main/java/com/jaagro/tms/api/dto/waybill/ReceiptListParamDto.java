package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class ReceiptListParamDto implements Serializable {
    /**
     * 运单状态
     */
    private String waybillStatus;
    /**
     * 车辆id
     */
    private Integer truckId;
    /**
     * 司机id
     */
    private Integer driverId;
}
