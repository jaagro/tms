package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class Waybill implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer orderId;

    /**
     * 装货地id
     */
    private Integer loadSiteId;

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
    private Integer needTruckType;

    /**
     * 车辆id
     */
    private Integer truckId;

    /**
     * 司机id
     */
    private Integer driverId;

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
    private Integer createdUserId;

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
     * 要求装货时间
     */
    private Date loadTime;

    /**
     * 
     */
    private Integer departmentId;

}