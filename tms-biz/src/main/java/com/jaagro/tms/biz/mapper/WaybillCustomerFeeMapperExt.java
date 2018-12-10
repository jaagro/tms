package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillFeeCondition;
import com.jaagro.tms.biz.entity.WaybillCustomerFee;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillCustomerFeeMapperExt extends WaybillCustomerFeeMapper {

    /**
     * 根据条件查询客户的运单费用
     *
     * @param condition
     * @return
     * @author @Gao.
     */
    List<WaybillCustomerFeeDto> listWaybillCustomerFeeByCondition(WaybillFeeCondition condition);

    /**
     * 根据异常id 查询客户侧费用
     *
     * @param anomalyId
     * @return
     */
    WaybillCustomerFee selectByAnomalyId(Integer anomalyId);
}