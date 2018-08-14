package com.jaagro.tms.biz.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tony
 */
@Data
public class Branch {

    private Long branchId;

    private Long tenantId;

    private String name;

    private String contactPerson;

    private String contactNumber;

    private Long adminRegionId;

    private String contactAddress;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String notes;

    private Long branchTypeId;

    private Long businessCharacterId;

    private Long upperLevelBranchId;

    private Date createdDate;

    private Long createdUserId;

    private Boolean isDeleted;

    private Boolean isActive;

    private Long version;
}