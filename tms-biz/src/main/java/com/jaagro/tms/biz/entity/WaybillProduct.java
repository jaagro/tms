package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class WaybillProduct {
    private Long waybillProductId;

    private Long waybillId;

    private String productName;

    private Integer quantity;

    private BigDecimal weight;

    private BigDecimal chargeableWeight;

    private Integer pickupQuantity;

    private BigDecimal pickupWeight;

    private BigDecimal pickupChargeableWeight;

    private Integer signedQuantity;

    private BigDecimal signedWeight;

    private BigDecimal signedChargeableWeight;

    private String shippingAddress;

    private Long shippingAddressId;

    private String contactPerson;

    private String contactNumber;

    private String detailedAddress;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private BigDecimal insuranceCost;

    private Date requiredDeliveryDatetime;

    private Long pricingMethodId;

    private BigDecimal billingMileage;

    private BigDecimal shippingUnitPrice;

    private BigDecimal shippingCostTotal;

    private BigDecimal pickupCharge;

    private BigDecimal deliveryCharge;

    private BigDecimal insuranceCharge;

    private BigDecimal otherCharge;

    private String notes;

    private Long receiptStatusId;

    private Integer receiptCount;

    private Boolean isDeleted;

    private Boolean isActive;

    private Long version;
}