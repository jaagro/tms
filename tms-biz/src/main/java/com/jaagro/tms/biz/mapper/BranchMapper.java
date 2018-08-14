package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Branch;

public interface BranchMapper {

    int deleteByPrimaryKey(Long branchId);

    int insert(Branch record);

    int insertSelective(Branch record);

    Branch selectByPrimaryKey(Long branchId);

    int updateByPrimaryKeySelective(Branch record);

    int updateByPrimaryKey(Branch record);

    Branch getBranchByUser(Long userId);
}