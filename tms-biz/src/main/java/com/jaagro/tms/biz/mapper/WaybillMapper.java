package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.driverapp.GetWaybillAppDto;
import com.jaagro.tms.biz.entity.Waybill;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillMapper {
    /**
     * 删除
     * @mbggenerated 2018-08-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insert(Waybill record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insertSelective(Waybill record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    Waybill selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKeySelective(Waybill record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKey(Waybill record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    List<Waybill> selectByOrderId(Integer orderld);

    /**
     * 查询未完成的运单
     * @param record
     * @return
     */
    List<GetWaybillAppDto> selectWaybillByCarrierStatus(Waybill record);

    /**
     * 查询完成与取消的运单
     * @param record
     * @return
     */
    List<GetWaybillAppDto> selectWaybillByStatus(Waybill record);

    /**
     * 根据orderId获取waybillId的list
     * @param orderId
     * @return
     */
    List<Integer> listWaybillIdByOrderId(Integer orderId);

}