package com.jaagro.tms.api.dto.waybill.driverapp;

import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author @gao
 */
@Data
@Accessors(chain = true)
public class GetWaybillDetailsAppDto implements Serializable {

    private Integer id;
    /**
     * 运单状态：待派车、待司机接单、已接单、司机已出发、司机到达装货地、运输中、卸货完成、已完成，取消
     */
    private String waybillStatus;
    /**
     * 运单号
     */
    private Integer waybillId;
    /**
     * 运单轨迹
     */
    private List<ShowTrackingDto> tracking;
    /**
     * 客户
     */
    private ShowCustomerDto customer;
    /**
     * 装货地
     */
    private ShowSiteDto loadSite;
    /**
     * 卸货地列表
     */
    private List<ShowSiteDto> unloadSite;

}
