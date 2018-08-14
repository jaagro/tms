package com.jaagro.tms.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tony
 */
@Data
public class PickUpAddressReturnDto implements Serializable {

    private Long pickUpAddressId;

    private Long customerId;

    private String addressCode;

    private String addressName;

    private String contactPerson;

    private String contactNumber;

    private Long adminRegionId;

    private String address;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String notes;

    private Date createdDate;

    private Long createdUserId;

    private Boolean isDeleted;

    private Boolean isActive;

    private Boolean isDefault;

    private Long version;
}
