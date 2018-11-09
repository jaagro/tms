package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.fee.WaybillFeeCondition;
import com.jaagro.tms.api.dto.fee.WaybillTruckFeeDto;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillTruckFeeMapperExt extends WaybillTruckFeeMapper {

    /**
     * 根据条件查询运力侧费用
     * @Gao.
     * 根据条件查询运力侧费用
     * @param condition
     * @return
     */
    List<WaybillTruckFeeDto> listWaybillTruckFeeByCondition(WaybillFeeCondition condition);

}