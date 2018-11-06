package com.jaagro.tms.api.dto.fee;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class WaybillTruckFeeDto implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer waybillId;

    /**
     *
     */
    private Integer waybillItemId;

    /**
     *
     */

    private Integer anomalyId;

    /**
     * 费用类型：1-运费 2-附加费
     */
    private Integer costType;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createdUserId;

    //-----WaybillFeeAdjustment字段-------
    /**
     * 调整原因类型：1-卸货费  2-货损费。。。
     */
    private Integer adjustReason;

    /**
     * 调整类型：1-货主向司机付费，2-司机向货主付费
     */
    private Integer adjustType;

    /**
     * 是否对客户有效
     */
    private Boolean customerEffect;

    /**
     * 是否对司机有效
     */
    private Boolean truckEffect;

    /**
     * 备注信息
     */
    private String notes;

}
