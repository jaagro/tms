package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.anomaly.*;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillFeeCondtion;
import com.jaagro.tms.api.dto.fee.WaybillTruckFeeDto;

import java.util.List;

/**
 * @author @Gao.
 */
public interface WaybillAnomalyService {

    /**
     * 司机运单异常申报
     * Author @Gao.
     */
    void waybillAnomalySubmit(WaybillAnomalyReportDto dto);

    /**
     * 运单异常类型显示
     * Author @Gao.
     */
    List<WaybillAnomalTypeDto> displayAnormalType();

    /**
     * 根据运单Id查询客户信息
     *
     * @param waybillId Author @Gao.
     * @return
     */
    ShowCustomerDto getCustomerByWaybillId(Integer waybillId);

    /**
     * 根据运单号id 查询异常信息
     *
     * @param
     * @return
     */
    List<WaybillAnomalyDto> listWaybillAnomalyByCondition(WaybillAnomalyCondtion dto);

    /**
     * 根据条件查询图片信息
     * Author @Gao.
     *
     * @param
     * @return
     */
    List<WaybillAnomalyImageDto> listWaybillAnormalyImageByCondtion(WaybillAnomalyImageCondtion dto);

    /**
     * 异常信息处理
     * Author @Gao.
     *
     * @param dto
     * @return
     */
    void anormalInformationProcess(AnomalInformationProcessDto dto);

    /**
     * 异常管理列表
     * Author @Gao.
     *
     * @param dto
     * @return
     */
    PageInfo anomalManagementList(WaybillAnomalyCondtion dto);

    /**
     * 客户侧费用
     */

    List<WaybillCustomerFeeDto> listWaybillCustomerFeeByCondtion(WaybillFeeCondtion dto);

    /**
     * 运力侧费用
     */
    List<WaybillTruckFeeDto> listWaybillTruckFeeByCondtion(WaybillFeeCondtion dto);
}
