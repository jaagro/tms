package com.jaagro.tms.api.dto.driverapp;

import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ListWaybillAppDto implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer orderId;
    /**
     * 接单时间
     */
    private Date singleTime;

    /**
     * 运单号
     */
    private Integer waybillId;

    /**
     * 装货地
     */
    private ShowSiteDto loadSite;

    /**
     * 客户
     */
    private ShowCustomerDto customer;

    /**
     * 货物类型
     */
    private Integer productType;

    /**
     * 运单状态：待派车、待司机接单、已接单、司机已出发、司机到达装货地、运输中、卸货完成、已完成，取消
     */
    private String waybillStatus;

    /**
     * 车辆
     */
    private ShowTruckDto truck;

    /**
     * 司机
     */
    private ShowDriverDto driver;

    /**
     * 任务推送司机时间
     */
    private Date sendTime;

    /**
     * 实际公里数
     */
    private BigDecimal distance;

    /**
     * 卸货地列表
     */
    private List<ShowSiteDto> unloadSite;

    /**
     * 货物列表
     */
    private List<ShowGoodsDto> goods;
}
