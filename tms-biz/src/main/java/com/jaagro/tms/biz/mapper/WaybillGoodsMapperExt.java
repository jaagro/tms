package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.driverapp.ShowGoodsDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillGoodsDto;
import com.jaagro.tms.biz.entity.WaybillGoods;
import org.apache.ibatis.annotations.Param;

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
     * 根据运单id获取商品信息
     *
     * @param id
     * @return
     */
    List<ShowGoodsDto> listWaybillGoodsByWaybillItemId(Integer id);

    /**
     * 批量更新运单货物
     * @author yj
     * @param waybillGoodsList
     * @return
     */
    Integer batchUpdateByPrimaryKeySelective(@Param("waybillGoodsList") List<WaybillGoods> waybillGoodsList);
}