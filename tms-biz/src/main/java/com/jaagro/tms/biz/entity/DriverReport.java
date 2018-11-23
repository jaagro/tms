package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 司机报表
 * @author yj
 * @since 20181122
 */
@Data
@Accessors
public class DriverReport implements Serializable{
    /**
     * 司机报表id
     */
    private Integer id;

    /**
     * 统计时间(yyyy-mm-dd)
     */
    private Date reportTime;

    /**
     * 司机名称
     */
    private String driverName;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 车牌号
     */
    private String truckNumber;

    /**
     * 车辆id
     */
    private Integer truckId;

    /**
     * 接单量
     */
    private Integer receiveWaybillQuantity;

    /**
     * 拒单量
     */
    private Integer refuseWaybillQuantity;

    /**
     * 异常量
     */
    private Integer anomalyWaybillQuantity;

    /**
     * 完成运单数
     */
    private Integer completeWaybillQuantity;

    /**
     * 平均接单时长(单位分钟)
     */
    private BigDecimal avgReceiveDuration;

    /**
     * 装货准时率
     */
    private BigDecimal loadPunctualityRate;

    /**
     * 卸货准时率
     */
    private BigDecimal unloadPunctualityRate;

    /**
     * 运费
     */
    private BigDecimal freight;

    /**
     * 异常费用
     */
    private BigDecimal anomalyCost;

    /**
     * 费用合计
     */
    private BigDecimal totalCost;

    /**
     * 是否有效(0-无效,1-有效)
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date modifyTime;

}