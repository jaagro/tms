package com.jaagro.tms.web.vo.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AnomalTypeVo implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 异常类型
     */
    private String typeName;
}
