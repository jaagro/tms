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
     * 根据运单id获取商品名称
     *
     * @param waybillId
     * @return
     */
    List<String> listGoodsNameByWaybillId(Integer waybillId);

    /**
     * 批量更新运单货物
     *
     * @param waybillGoodsList
     * @return
     * @author yj
     */
    Integer batchUpdateByPrimaryKeySelective(@Param("waybillGoodsList") List<WaybillGoods> waybillGoodsList);

    /**
     * 根据运单id删除
     *
     * @param waybillId
     * @return
     * @author yj
     */
    Integer deleteByWaybillId(@Param("waybillId") Integer waybillId);

    /**
     * 批量插入
     *
     * @param waybillGoodsList
     * @return
     * @author yj
     */
    Integer batchInsert(@Param("waybillGoodsList") List<WaybillGoods> waybillGoodsList);

    /**
     * 根据运单id逻辑删除相关的waybillGoods记录
     *
     * @param waybillId
     * @return
     */
    Integer disableWaybillGoodsByWaybillId(@Param("waybillId") Integer waybillId);

    /**
     * 根据运单id获取数量或重量的总值
     *
     * @param waybillId
     * @return
     */
    WaybillGoods getQuantityAndWeightByWaybillId(@Param("waybillId") Integer waybillId);

}