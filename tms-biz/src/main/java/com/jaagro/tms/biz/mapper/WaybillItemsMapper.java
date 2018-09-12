package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillItems;

import java.util.List;

public interface WaybillItemsMapper {
    /**
     *
     * @mbggenerated 2018-08-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insert(WaybillItems record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insertSelective(WaybillItems record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    WaybillItems selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKeySelective(WaybillItems record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKey(WaybillItems record);

    /**
     * 通过waybillId获取运单明细
     * @param waybillId
     * @return
     */
    List<WaybillItems> listWaybillItemsByWaybillId(Integer waybillId);
}