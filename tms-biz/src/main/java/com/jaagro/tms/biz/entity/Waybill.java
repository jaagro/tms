package com.jaagro.tms.biz.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tony
 */
@Data
public class Waybill {

    private Long waybillId;

    private Long branchId;

    private Long userId;

    private Date acceptedDate;

    private Date createdDate;

    private Long createdUserId;

    private String waybillCode;

    private String customerCode;

    private String customerName;

    private Long customerId;

    private String pickUpAddress;

    private Long pickUpAddressId;

    private Long productTypeId;

    private Long transportationTypeId;

    private String contactPerson;

    private String contactNumber;

    private String detailedAddress;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Long paymentTypeId;

    private Date requiredPickupDatetime;

    private String notes;

    private Long waybillStatusId;

    private Long waybillValidatingStatusId;

    private Date validatedDate;

    private Long validatedUserId;

    private Date settlementDate;

    private Long settlementUserId;

    private Integer shippedQuantity;

    private Boolean isDeleted;

    private Boolean isActive;

    private Boolean isUrgent;

    private Long version;
}