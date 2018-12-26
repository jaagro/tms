package com.jaagro.tms.api.dto.customer;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author gavin
 * 20181219
 */
@Data
@Accessors(chain = true)
public class CalculatePaymentDto implements Serializable {

    private static final long serialVersionUID = 6213506945138061463L;
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
     * 运单装卸货地ID列表对象
     */
    private List<SiteDto> siteDtoList;


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
