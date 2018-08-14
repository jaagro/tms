package com.jaagro.tms.biz.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author tony
 */
@Data
public class UserBranch {

    private Long userBranchId;

    private Long userId;

    private Long branchId;

    private Date createdDate;

    private Long createdUserId;

    private Long version;
}