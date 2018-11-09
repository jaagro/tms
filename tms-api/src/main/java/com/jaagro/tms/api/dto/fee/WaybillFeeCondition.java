package com.jaagro.tms.api.dto.fee;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class WaybillFeeCondition implements Serializable {
    /**
     * 异常Id
     */
    private Integer anomalyId;
}
