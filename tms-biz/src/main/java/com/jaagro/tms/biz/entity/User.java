package com.jaagro.tms.biz.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tony
 */
@Data
public class User {
    private Long userId;

    private Long tenantId;

    private String userCode;

    private String username;

    private String password;

    private String notes;

    private String displayName;

    private Date createdDate;

    private Long createdUserId;

    private BigDecimal ratingScore;

    private Integer ratingCounts;

    private Boolean isDeleted;

    private Boolean isActive;

    private Long version;

    private String contactNumber;
}