package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author @Gao.
 * 异常管理列表
 */
@Data
@Accessors(chain = true)
public class AnomalManagementListDto implements Serializable {

    /**
     * 异常类型
     */
    private String typeName;

    /**
     * 处理状态
     */
    private String processStatus;

    /**
     * 扣款金额
     */
    private String deductMoney;

    /**
     * 赔款金额
     */
    private String  compensateMoney;

    /**
     * 运单编号
     */
    private Integer waybillId;

    /**
     * 登记时间
     */
    private Integer createTime;

    /**
     * 登记人
     */
    private Integer creatorName;
    /**
     * 处理人
     */
    private  Integer processorName;

}
