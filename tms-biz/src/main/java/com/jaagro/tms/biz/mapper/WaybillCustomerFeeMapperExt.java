package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillFeeCondtion;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillCustomerFeeMapperExt extends WaybillCustomerFeeMapper{

    /**
     *  @author @Gao.
     * 根据条件查询客户费用相关信息
     * @return
     */
    List<WaybillCustomerFeeDto > listWaybillCustomerFeeByCondition(WaybillFeeCondtion condtion);

}