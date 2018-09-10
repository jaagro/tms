package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.GetWaybillAppDto;
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
}