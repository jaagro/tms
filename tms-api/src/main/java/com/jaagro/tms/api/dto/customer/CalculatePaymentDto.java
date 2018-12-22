package com.jaagro.tms.api.dto.customer;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author gavin
 * 20181219
 */
@Data
@Accessors(chain = true)
public class CalculatePaymentDto implements Serializable {

    /**
     * 运单Id
     */
    @NotNull(message = "{waybillId.NotNull}")
    private Integer waybillId;
    /**
     * 运单完成时间，必须完成的运单才能有结算金额
     */
    private Date doneDate;
    /**
     * 合同id
     */
    @NotNull(message = "{contractId.NotNull}")
    private Integer contractId;

    /**
     * 货物类型
     */

    @NotNull(message = "{goodsType.NotNull}")
    private Integer productType;

    /**
     * 装货地址id
     */
    @NotNull(message = "{loadSiteId.NotNull}")
    private Integer loadSiteId;

    /**
     * 卸货地址id
     */
    @NotNull(message = "{unloadSiteId.NotNull}")
    private Integer unloadSiteId;

    /**
     * 车辆类型
     */
    @NotNull(message = "{truckType.NotNull}")
    private Integer truckTypeId;

    /**
     * 结算重量（吨）
     */
    private BigDecimal unloadWeight;

    /**
     * 需计算的数量(头)
     */
    private Integer unloadQuantity;

    /**
     * 实际的里程
     */
    private BigDecimal actualMileage;

}
