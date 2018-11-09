package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class WaybillAnomalyTypeDto implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 异常类型
     */
    private String typeName;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 逻辑删除
     */
    private Boolean enabled;
}
