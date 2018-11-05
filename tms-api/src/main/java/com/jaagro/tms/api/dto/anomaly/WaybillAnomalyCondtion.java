package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class WaybillAnomalyCondtion implements Serializable {
    /**
     * 异常Id
     */
    private Integer id;
    /**
     * 异常创建人Id
     */
    private Integer createUserId;

    /**
     * 运单号Id
     */
    private Integer waybillId;
}
