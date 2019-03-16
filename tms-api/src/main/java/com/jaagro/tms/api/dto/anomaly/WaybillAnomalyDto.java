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
public class WaybillAnomalyDto implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 异常类型id
     */
    private Integer anomalyTypeId;

    /**
     * 异常描述
     */
    private String anomalyDesc;

    /**
     * 处理状态
     */
    private String processingStatus;

    /**
     * 处理描述
     */
    private String processingDesc;

    /**
     * 审核状态：待审核、已审核、拒绝
     */
    private String auditStatus;

    /**
     * 审核描述
     */
    private String auditDesc;

    /**
     * 情况是否属实 0-否 1-是
     */
    private Boolean verifiedStatus;

    /**
     * 是否涉及费用调整  0-否 1-是
     */
    private Boolean adjustStatus;

    /**
     * 登记人的id
     */
    private Integer createUserId;

    /**
     * 创建人类型
     */
    private String createUserType;

    /**
     * 登记时间
     */
    private Date createTime;

    /**
     * 处理人的id
     */
    private Integer processorUserId;

    /**
     * 处理时间
     */
    private Date processorTime;

    /**
     *
     */
    private Integer auditUserId;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 逻辑删除
     */
    private Boolean enabled;
    /**
     * 异常类型
     */
    private String typeName;
    /**
     * 扣款金额
     */
    private String deductMoney;
    /**
     * 赔款金额
     */
    private String compensateMoney;

    /**
     * 登记人
     */
    private String creatorName;

    /**
     * 处理人
     */
    private String processorName;

    /**
     * 审核人
     */
    private String auditName;

    /**
     * 网点id
     */
    private Integer networkId;

    /**
     * 项目部名称
     */
    private String projectDeptName;
}
