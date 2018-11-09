package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.anomaly.*;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillFeeCondition;
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
    List<WaybillAnomalyTypeDto> displayAnomalyType();

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
    List<WaybillAnomalyDto> listWaybillAnomalyByCondition(WaybillAnomalyCondition dto);

    /**
     * 根据条件查询图片信息
     * Author @Gao.
     *
     * @param
     * @return
     */
    List<WaybillAnomalyImageDto> listWaybillAnomalyImageByCondition(WaybillAnomalyImageCondition dto);

    /**
     * 异常信息处理
     * Author @Gao.
     *
     * @param dto
     * @return
     */
    void anomalyInformationProcess(AnomalyInformationProcessDto dto);

    /**
     * 异常管理列表
     * Author @Gao.
     *
     * @param dto
     * @return
     */
    PageInfo anomalyManagementList(WaybillAnomalyCondition dto);

    /**
     * 客户侧费用
     * @param dto
     * @return
     */

    List<WaybillCustomerFeeDto> listWaybillCustomerFeeByCondition(WaybillFeeCondition dto);

    /**
     * 运力侧费用
     * @param dto
     * @return
     */
    List<WaybillTruckFeeDto> listWaybillTruckFeeByCondition(WaybillFeeCondition dto);

    /**
     * 改变异常状态:入参为已处理则将状态改为待审核，入参为待审核则将状态改为已处理
     * @param nowStatus 当前状态
     * @param id
     * @return
     */
    boolean changeAnomalyStatus(int[] id, String nowStatus);
}
