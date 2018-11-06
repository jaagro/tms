package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.fee.WaybillFeeCondtion;
import com.jaagro.tms.api.dto.fee.WaybillTruckFeeDto;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillTruckFeeMapperExt extends WaybillTruckFeeMapper {

    /**
     * @Gao.
     * 根据条件查询运力侧费用
     * @param condtion
     * @return
     */
    List<WaybillTruckFeeDto> listWaybillTruckFeeByCondtion(WaybillFeeCondtion condtion);

}