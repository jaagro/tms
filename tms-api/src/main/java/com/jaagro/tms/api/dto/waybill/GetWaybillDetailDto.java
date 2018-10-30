package com.jaagro.tms.api.dto.waybill;

import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.base.ShowUserDto;
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
 * @author Gavin
 */
@Data
@Accessors(chain = true)
public class GetWaybillDetailDto implements Serializable {

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
     * 装货地id
     */
    private Integer loadSiteId;
    /**
     * 装货地
     */
    private ShowSiteDto loadSiteDto;

    /**
     * 运单状态：待派车、待司机接单、已接单、司机已出发、司机到达装货地、运输中、卸货完成、已完成，取消
     */
    private String waybillStatus;

    /**
     * 需求车型
     */
    private Integer needTruckTypeId;
    /**
     * 需求车型
     */
    private ListTruckTypeDto needTruckTypeDto;
    /**
     * 运单指派的车辆id
     */
    private Integer truckId;
    /**
     * 运单指派的车辆详情
     */
    private ShowTruckDto assginedTruckDto;
    /**
     * 接单司机id
     */
    private Integer driverId;
    /**
     * 司机详情
     */
    private ShowDriverDto driverDto;
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
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人id
     */
    private Integer modifyUserId;

    /**
     * 是否有效
     */
    private Boolean enabled;
    /**
     * 运单明细list
     */
    private List<GetWaybillItemDto> waybillItems;

    /**
     * 运单轨迹
     */
    private List<GetTrackingDto> tracking;

    /**
     * 装货时间
     */
    private Date loadTime;
    /**
     * 货物类型
     */
    private Integer goodType;
    /**
     *
     */
    private Integer departmentId;
}
