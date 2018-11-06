package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class WaybillTruckFee implements Serializable {
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