package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.driverapp.GetWaybillAppDto;
import com.jaagro.tms.api.dto.driverapp.ListWaybillAppDto;
import com.jaagro.tms.api.dto.waybill.ListWaybillCriteriaDto;
import com.jaagro.tms.api.dto.waybill.ListWaybillDto;
import com.jaagro.tms.api.dto.waybill.ListWebChatWaybillCriteriaDto;
import com.jaagro.tms.api.dto.waybill.ReceiptListParamDto;
import com.jaagro.tms.biz.entity.Waybill;
import org.apache.ibatis.annotations.Param;

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
     * 根据订单id获取运单
     *
     * @param orderId
     * @return
     */
    List<ListWaybillDto> listWaybillDtoByOrderId(Integer orderId);

    /**
     * 查询未完成的运单
     *
     * @param record
     * @return
     */
    List<ListWaybillAppDto> selectWaybillByCarrierStatus(Waybill record);

    /**
     * 查询完成与取消的运单
     *
     * @param record
     * @return
     */
    List<GetWaybillAppDto> selectWaybillByStatus(Waybill record);

    /**
     * 根据状态查询订单
     *
     * @param record
     * @return
     */
    List<ListWaybillAppDto> getWaybillByStatus(Waybill record);

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

    /**
     * 根据订单id查询 待派单的运单
     *
     * @param orderId
     * @return
     */
    List<ListWaybillDto> listWaybillDtoWaitByOrderId(Integer orderId);

    /**
     * 选择派单超过30分钟的运单 gavin
     *
     * @return
     */
    List<Waybill> listOverTimeWaybills();

    /**
     * 批量更新运单状态为拒绝
     *
     * @param waybills
     */

    void batchUpdateWaybillStatus(List<Waybill> waybills);

    /**
     * 查询出司机中所有在承运中的订单
     *
     * @param waybill
     * @return
     */
    List<ListWaybillDto> listCriteriaWaybill(Waybill waybill);

    /**
     * 根据订单id查询已拒单的个数
     *
     * @param orderId
     * @return
     */
    Integer listRejectWaybillByOrderId(@Param("orderId") Integer orderId);

    /**
     * 根据id 查询未删除 未作废的 运单
     *
     * @param id
     * @return
     */
    Waybill getWaybillById(Integer id);

    /**
     * @param id
     * @return
     */
    Integer listWaitWaybillByOrderId(Integer id);

    /**
     * 根据当前用户查询运单列表
     *
     * @param criteriaDto
     * @return
     */
    List<ListWaybillDto> listWebChatWaybillByCriteria(ListWebChatWaybillCriteriaDto criteriaDto);

    /**
     * 根据司机id统计未完成的运单数说
     *
     * @param driverId
     * @return
     */
    Integer countUnDoneByDriverId(@Param("driverId") Integer driverId);

    /**
     * 异常运单重置派单
     *
     * @param waybill
     */
    void updateCancelWaybillById(Waybill waybill);

    /**
     * 查询接单超时的运单，便于短信提醒
     *
     * @param waybillStatus
     * @return
     */
    List<Waybill> listWaybillTimeOut(@Param("waybillStatus") String waybillStatus);

    /**
     * 根据查询条件查询运单接单列表
     * @param param
     * @return
     */
    List<GetWaybillAppDto> listWaybillByStatus(ReceiptListParamDto param);

    /**
     * 查询需要提醒司机出发的运单
     * @return
     */
    List<Waybill> listToStartInform();

    /**
     * 司机到达装货地超时的运单
     * @return
     */
    List<Waybill> listWaybillLoadLate();

    /**
     * 司机到达卸货地超时的运单
     * @return
     */
    List<Waybill> listWaybillUnLoadLate();
}
