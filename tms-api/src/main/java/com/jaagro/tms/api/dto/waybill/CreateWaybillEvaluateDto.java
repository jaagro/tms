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
public class CreateWaybillEvaluateDto implements Serializable {
    /**
     * 运单id
     */
    private Integer waybillId;
    /**
     * 司机id
     */
    private Integer driverId;
    /**
     * 备注
     */
    private String note;
    /**
     *
     */
    List<CreateEvaluateTypeDto> createEvaluateType;


}
