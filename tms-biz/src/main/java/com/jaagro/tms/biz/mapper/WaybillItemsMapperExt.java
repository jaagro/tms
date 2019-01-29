package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.CountUnFinishWaybillCriteriaDto;
import com.jaagro.tms.biz.entity.WaybillItems;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
public interface WaybillItemsMapperExt extends WaybillItemsMapper {
    /**
     * 通过waybillId获取运单明细
     *
     * @param waybillId
     * @return
     */
    List<WaybillItems> listWaybillItemsByWaybillId(Integer waybillId);

    /**
     * 根据waybillId 签收状态 获取卸货地id
     *
     * @param waybillItems
     * @return
     */
    List<Map<String, Long>> listWaybillIdIdAndSignStatus(WaybillItems waybillItems);

    /**
     * 批量更新运单卸货地
     *
     * @param waybillItemsList
     * @return
     * @author yj
     */
    Integer batchUpdateByPrimaryKeySelective(@Param("waybillItemsList") List<WaybillItems> waybillItemsList);

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
     * @param waybillItemsList
     * @return
     * @author yj
     */
    Integer batchInsert(@Param("waybillItemsList") List<WaybillItems> waybillItemsList);

    /**
     * 根据合同，装货地，卸货地查询未完成的运单数
     *
     * @param criteriaDto
     * @return
     * @author baiyiran
     */
    Integer countUnFinishWaybillByContract(CountUnFinishWaybillCriteriaDto criteriaDto);
}