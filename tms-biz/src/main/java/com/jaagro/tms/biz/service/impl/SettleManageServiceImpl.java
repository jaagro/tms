package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.settlemanage.ReturnWaybillFeeDto;
import com.jaagro.tms.api.dto.settlemanage.WaybillFeeCriteria;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.service.SettleManageService;
import com.jaagro.tms.biz.entity.WaybillTracking;
import com.jaagro.tms.biz.mapper.WaybillMapperExt;
import com.jaagro.tms.biz.mapper.WaybillTrackingMapperExt;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.TruckClientService;
import com.jaagro.tms.biz.service.UserClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 结算管理服务
 * @author: @Gao.
 * @create: 2019-04-11 15:48
 **/
@Service
public class SettleManageServiceImpl implements SettleManageService {

    @Autowired
    private WaybillMapperExt waybillMapper;
    @Autowired
    private CustomerClientService customerClientService;
    @Autowired
    private WaybillTrackingMapperExt waybillTrackingMapper;
    @Autowired
    private TruckClientService truckClientService;
    @Autowired
    private UserClientService userClientService;

    /**
     * 运单结算费用报表
     *
     * @param criteria
     * @return
     */
    @Override
    public PageInfo listWaybillFee(WaybillFeeCriteria criteria) {
        List<ReturnWaybillFeeDto> returnWaybillFeeDtos = waybillMapper.listSettleManageWaybillFee(criteria);
        for (ReturnWaybillFeeDto returnWaybillFeeDto : returnWaybillFeeDtos) {
            //客户名称
            if (returnWaybillFeeDto.getCustomerId() != null) {
                ShowCustomerDto customer = customerClientService.getShowCustomerById(returnWaybillFeeDto.getCustomerId());
                if (customer != null && customer.getCustomerName() != null) {
                    returnWaybillFeeDto.setCustomerName(customer.getCustomerName());
                }
            }
            //获取装货地
            ShowSiteDto showSite = customerClientService.getShowSiteById(returnWaybillFeeDto.getLoadSiteId());
            if (showSite != null && showSite.getSiteName() != null) {
                WaybillTracking loadSiteInfo = waybillTrackingMapper.getLoadSiteInfoByWaybillId(returnWaybillFeeDto.getWaybillId());
                if (loadSiteInfo != null) {
                    returnWaybillFeeDto
                            .setLoadSiteTime(loadSiteInfo.getCreateTime())
                            .setLoadSiteName(showSite.getSiteName());
                }
            }
            //获取卸货地
            if (returnWaybillFeeDto.getWaybillId() != null) {
                WaybillTracking unloadSiteInfo = waybillTrackingMapper.getUnLoadSiteInfoByWaybillId(returnWaybillFeeDto.getWaybillId());
                if (unloadSiteInfo.getTrackingInfo() != null) {
                    String trackingInfo = unloadSiteInfo.getTrackingInfo();
                    int start = trackingInfo.indexOf("【") + 1;
                    int end = trackingInfo.indexOf("】");
                    returnWaybillFeeDto
                            .setUnLoadSiteTime(unloadSiteInfo.getCreateTime())
                            .setUnLoadSiteName(trackingInfo.substring(start, end));
                }
            }
            //获取车牌号
            if (returnWaybillFeeDto.getTruckId() != null) {
                ShowTruckDto truck = truckClientService.getTruckByIdReturnObject(returnWaybillFeeDto.getTruckId());
                if (truck != null && truck.getTruckNumber() != null) {
                    returnWaybillFeeDto.setTruckNumber(truck.getTruckNumber());
                }
            }
            //部门名称
            if (returnWaybillFeeDto.getNetWorkId() != null) {
                String deptName = userClientService.getDeptNameById(returnWaybillFeeDto.getNetWorkId());
                returnWaybillFeeDto.setNetWorkName(deptName);
            }
        }
        return null;
    }
}
