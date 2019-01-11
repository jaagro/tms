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
public class GrabWaybillParamDto implements Serializable {
    /**
     * 运单id
     */
    private Integer waybillId;
    /**
     * 车辆id
     */
    private List<Integer> truckIds;

}
