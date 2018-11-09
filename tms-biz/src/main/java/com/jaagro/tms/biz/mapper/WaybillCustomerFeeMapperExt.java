package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillFeeCondition;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillCustomerFeeMapperExt extends WaybillCustomerFeeMapper{

    /**
     * 根据条件查询客户的运单费用
     *  @author @Gao.
     * @param condition
     * @return
     */
    List<WaybillCustomerFeeDto > listWaybillCustomerFeeByCondition(WaybillFeeCondition condition);

}