package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.UserBranch;

public interface UserBranchMapper {
    int deleteByPrimaryKey(Long userBranchId);

    int insert(UserBranch record);

    int insertSelective(UserBranch record);

    UserBranch selectByPrimaryKey(Long userBranchId);

    int updateByPrimaryKeySelective(UserBranch record);

    int updateByPrimaryKey(UserBranch record);
}