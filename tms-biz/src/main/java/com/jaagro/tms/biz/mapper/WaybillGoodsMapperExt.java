package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.driverapp.UpdateGoodDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillGoodsDto;
import com.jaagro.tms.biz.entity.WaybillGoods;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillGoodsMapperExt extends WaybillGoodsMapper {

    /**
     * 根据运单id获取所有goods列表
     *
     * @param waybillId
     * @return
     */
    List<GetWaybillGoodsDto> listGoodsByWaybillId(Integer waybillId);

    /**
     * 根据明细id获取goods列表
     *
     * @param itemId
     * @return
     */
    List<WaybillGoods> listWaybillGoodsByItemId(Integer itemId);

    /**
     * 根据货物id 更新装货重量
     * @param updateGoodDto
     */
    void updateGoodLoadWeightById(UpdateGoodDto updateGoodDto);


}