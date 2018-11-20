package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class WaybillAnomalyImageCondition implements Serializable {

    /**
     * 异常id
     */
    private Integer anomalyId;

    /**
     * 创建人的Id
     */
    private Integer createUserId;

    /**
     * 异常图片类型
     */
    private Integer anomalyImageType;
}
