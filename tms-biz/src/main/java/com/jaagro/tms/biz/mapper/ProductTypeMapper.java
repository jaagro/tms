package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.ProductType;

public interface ProductTypeMapper {

    int deleteByPrimaryKey(Long productTypeId);

    int insert(ProductType record);

    int insertSelective(ProductType record);

    ProductType selectByPrimaryKey(Long productTypeId);

    int updateByPrimaryKeySelective(ProductType record);

    int updateByPrimaryKey(ProductType record);

    ProductType getProductByName(String name);
}