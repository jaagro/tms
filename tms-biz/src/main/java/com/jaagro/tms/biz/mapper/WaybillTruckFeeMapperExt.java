package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.fee.WaybillFeeCondition;
import com.jaagro.tms.api.dto.fee.WaybillTruckFeeDto;
import com.jaagro.tms.biz.entity.WaybillTruckFee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillTruckFeeMapperExt extends WaybillTruckFeeMapper {

    /**
     * 根据条件查询运力侧费用
     *
     * @param condition
     * @return
     * @Gao. 根据条件查询运力侧费用
     */
    List<WaybillTruckFeeDto> listWaybillTruckFeeByCondition(WaybillFeeCondition condition);

    /**
     * 根据异常id 查询运力侧费用
     * @param anomalyId
     * @return
     */
    WaybillTruckFee selectByAnomalyId(Integer anomalyId);

    /**
     * 批量插入
     * @param waybillTruckFeeList
     * @return
     */
    Integer batchInsert(@Param("waybillTruckFeeList") List<WaybillTruckFee> waybillTruckFeeList);
}