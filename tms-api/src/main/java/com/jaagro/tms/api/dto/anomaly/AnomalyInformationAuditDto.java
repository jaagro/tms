package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class AnomalyInformationAuditDto implements Serializable {
    /**
     * 异常id
     */
    private Integer id;
    /**
     * 审核结果
     */
    private String auditStatus;
    /**
     * 审核描述
     */
    private String auditDesc;
}
