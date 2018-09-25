package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.driverapp.GetWaybillAppDto;
import com.jaagro.tms.api.dto.waybill.ListWaybillCriteriaDto;
import com.jaagro.tms.api.dto.waybill.ListWaybillDto;
import com.jaagro.tms.biz.entity.Waybill;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tony
 */
public interface WaybillMapperExt extends WaybillMapper {

    /**
     * 从订单计划中移除waybill
     *
     * @param waybillId
     * @return
     */
    Integer removeWaybillById(Integer waybillId);

    /**
     * 根据订单id获取运单
     *
     * @param orderId
     * @return
     */
    List<Waybill> listWaybillByOrderId(Integer orderId);

    /**
     * 查询未完成的运单
     *
     * @param record
     * @return
     */
    List<GetWaybillAppDto> selectWaybillByCarrierStatus(Waybill record);

    /**
     * 查询完成与取消的运单
     *
     * @param record
     * @return
     */
    List<GetWaybillAppDto> selectWaybillByStatus(Waybill record);

    /**
     * 根据orderId获取waybillId的list
     *
     * @param orderId
     * @return
     */
    List<Integer> listWaybillIdByOrderId(Integer orderId);

    /**
     * 分页查询运单列表管理
     *
     * @param criteriaDto
     * @return
     */
    List<ListWaybillDto> listWaybillByCriteria(ListWaybillCriteriaDto criteriaDto);

    /**
     * 根据订单id查询 待派单的运单
     *
     * @param orderId
     * @return
     */
    List<Waybill> listWaybillWaitByOrderId(Integer orderId);
}
