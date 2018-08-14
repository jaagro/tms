package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.CustomerReturnDto;
import com.jaagro.tms.biz.entity.Customer;

public interface CustomerMapper {

    int deleteByPrimaryKey(Long customerId);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Long customerId);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);

    CustomerReturnDto getCustomerById(Long id);
}