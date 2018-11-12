package com.jaagro.tms.web.vo.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
/**
 * @Author @Gao.
 */
@Data
@Accessors(chain = true)
public class AnomalyAuditManagementListVo implements Serializable {
    /**
     * 异常id
     */
    private Integer id;

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
     * 运单编号
     */
    private Integer waybillId;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核人
     */
    private String auditName;

    /**
     * 审核人处理描述
     */
    private String auditDesc;

    /**
     * 处理结果
     */
    private String auditStatus;



}
