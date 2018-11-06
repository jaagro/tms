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
public class WaybillAnomalyCondtion implements Serializable {
    /**
     * 异常创建人Id
     */
    private Integer createUserId;
    /**
     * 运单号Id
     */
    private Integer waybillId;

    /**
     * 异常Id
     */
    private Integer anomalyId;

    /**
     * 异常类型id
     */
    private Integer anomalyTypeId;

    /**
     * 处理状态
     */
    private String processingStatus;

    /**
     * 登记开始时间
     */
    private Date beginDate;

    /**
     * 登记结束时间
     */
    private Date endDate;

    /**
     * 异常创建人 类型
     */
    private String createUserType;
}
