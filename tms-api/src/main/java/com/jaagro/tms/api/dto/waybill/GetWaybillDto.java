package com.jaagro.tms.api.dto.waybill;

import com.jaagro.tms.api.dto.base.ShowTruckTypeDto;
import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
     * 运单状态：待派车、待司机接单、已接单、司机已出发、司机到达装货地、运输中、卸货完成、已完成，取消
     */
    private String waybillStatus;

    /**
     * 需求车型
     */
    private ShowTruckTypeDto needTruckType;

    /**
     * 车辆id
     */
    private ShowTruckDto truckId;

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
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人id
     */
    private ShowUserDto modifyUserId;
}
