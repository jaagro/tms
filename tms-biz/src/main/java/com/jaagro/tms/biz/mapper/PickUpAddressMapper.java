package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.PickUpAddress;

public interface PickUpAddressMapper {

    int deleteByPrimaryKey(Long pickUpAddressId);

    int insert(PickUpAddress record);

    int insertSelective(PickUpAddress record);

    PickUpAddress selectByPrimaryKey(Long pickUpAddressId);

    int updateByPrimaryKeySelective(PickUpAddress record);

    int updateByPrimaryKey(PickUpAddress record);

    PickUpAddress getPickUpAddressByName(String addressName);
}