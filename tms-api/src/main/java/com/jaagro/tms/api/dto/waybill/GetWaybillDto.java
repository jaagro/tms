package com.jaagro.tms.api.dto.waybill;

import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.api.dto.customer.CustomerContactsReturnDto;
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
public class GetWaybillDto implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer orderId;

    /**
     * 车队合同id
     */
    private Integer truckTeamContractId;

    /**
     * 装货地
     */
    private ShowSiteDto loadSite;

    /**
     * 运单状态：待派车、待司机接单、已接单、司机已出发、司机到达装货地、运输中、卸货完成、已完成，取消
     */
    private String waybillStatus;

    /**
     * 需求车型
     */
    private ListTruckTypeDto needTruckType;

    /**
     * 车辆id
     */
    List<ShowTruckDto> showTruckDtos;

    /**
     * 司机id
     */
    private ShowDriverDto driverId;
    /**
     * 任务推送司机时间
     */
    private Date sendTime;

    /**
     * 实际公里数
     */
    private BigDecimal distance;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private ShowUserDto createdUserId;

    /**
     * 运单明细list
     */
    private List<GetWaybillItemDto> waybillItems;

    /**
     * 运单轨迹
     */
    private List<GetTrackingDto> tracking;

    private Integer totalQuantity;

    private BigDecimal totalWeight;

    /**
     * 装货时间
     */
    private Date loadTime;
    /**
     * 货物类型
     */
    private Integer goodType;
    /**
     * 订单的客户信息
     */
    private ShowCustomerDto customerDto;

    /**
     * 订单客户的联系人信息
     */
    private CustomerContactsReturnDto customerContactsDto;
    /**
     * 判断是否是抢单模式
     */
    private boolean GrabWaybillStatus;

    /**
     * 司机拒单理由
     */
    private GetRefuseTrackingDto refuseDto;
    /**
     * 客户网点
     */
    private Integer networkId;
    /**
     * 备注:包括作废理由
     */
    private String notes;
    /**
     * 修改时间
     */
    private Date modifyTime;
}
