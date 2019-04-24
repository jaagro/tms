package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class Location implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 设备类型 1-手机 2-车载GPS
     */
    private Integer deviceType;

    /**
     * 车牌号
     */
    private String truckNumber;

    /**
     * 设备信息(IMEI等)
     */
    private String deviceInfo;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 车辆ID
     */
    private Integer truckId;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 运单状态
     */
    private String waybillStatus;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 速度
     */
    private BigDecimal speed;

    /**
     * 角度
     */
    private BigDecimal angle;

    /**
     * 设备定位的时间
     */
    private Date locationTime;

    /**
     * 创建时间
     */
    private Date createTime;
}