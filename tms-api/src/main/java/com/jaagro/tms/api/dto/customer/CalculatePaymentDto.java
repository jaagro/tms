package com.jaagro.tms.api.dto.customer;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotEmpty;

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

    private static final long serialVersionUID = 5575206791439395639L;
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
     * 客户合同id
     */
    @NotNull(message = "{customerContractId.NotNull}")
    private Integer customerContractId;

    /**
     * 车队合同id
     */
    @NotNull(message = "{truckTeamContractId.NotNull}")
    private Integer truckTeamContractId;

    /**
     * 货物类型
     */

    @NotNull(message = "{goodsType.NotNull}")
    private Integer productType;


    /**
     * 运单装卸货地ID列表对象
     */
    @NotEmpty(message = "{siteDtoList.NotEmpty}")
    private List<SiteDto> siteDtoList;



    /**
     * 车辆类型
     */
    @NotNull(message = "{truckType.NotNull}")
    private Integer truckTypeId;

    /**
     * 客户结算重量（吨）
     */
    private BigDecimal customerCalWeight;

    /**
     * 客户需计算的数量(头)
     */
    private Integer customerCalQuantity;

    /**
     * 运力结算重量（吨）
     */
    private BigDecimal driverCalWeight;

    /**
     * 运力需计算的数量(头)
     */
    private Integer driverCalQuantity;

    /**
     * 实际的里程
     */
    private BigDecimal actualMileage;

}
