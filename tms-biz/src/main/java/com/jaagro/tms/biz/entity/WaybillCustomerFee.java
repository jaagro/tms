package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yj
 * @since 20181226
 */
@Data
@Accessors(chain = true)
public class WaybillCustomerFee implements Serializable {
    private static final long serialVersionUID = -1227597013918445565L;
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
     * 费用类型：1-运费 2-附加费
     */
    private Integer feeType;

    /**
     *
     */
    private Integer anomalyId;

    /**
     * 资金方向(1-增加,2-减少)
     */
    private Integer direction;

    /**
     * 结算状态(0-未结算,1-已结算,2-已支付)
     */
    private Integer settleStatus;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 是否有效
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createdUserId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人id
     */
    private Integer modifyUserId;

}