package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class WaybillAbnomalTypeDto implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 异常类型
     */
    private String typeName;
}
