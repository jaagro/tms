package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.GetWaybillGoodsDto;
import com.jaagro.tms.biz.entity.Waybill;
import com.jaagro.tms.biz.entity.WaybillGoods;

import java.util.List;

public interface WaybillGoodsMapper {
    /**
     *
     * @mbggenerated 2018-08-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insert(WaybillGoods record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insertSelective(WaybillGoods record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    WaybillGoods selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKeySelective(WaybillGoods record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKey(WaybillGoods record);

    /**
     * 根据明细id获取goods列表
     * @param itemId
     * @return
     */
    List<WaybillGoods> listWaybillGoodsByItemId(Integer itemId);
}