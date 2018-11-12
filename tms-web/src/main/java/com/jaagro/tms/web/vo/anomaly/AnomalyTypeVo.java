package com.jaagro.tms.web.vo.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 * @date
 */
@Data
@Accessors(chain = true)
public class AnomalyTypeVo implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 异常类型
     */
    private String typeName;
}
