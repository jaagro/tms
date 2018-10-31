package com.jaagro.tms.biz.entity;

import java.math.BigDecimal;
import java.util.Date;

public class WaybillTracking {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer waybillId;

    /**
     * 跟踪类型：1-运输轨迹 2-补录轨迹
     */
    private Integer trackingType;

    /**
     * 修改前状态
     */
    private String oldStatus;

    /**
     * 修改后状态
     */
    private String newStatus;

    /**
     * 司机id：若是APP操作修改，填写此处
     */
    private Integer driverId;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 轨迹描述
     */
    private String trackingInfo;

    /**
     * 发起修改设备序列号
     */
    private String device;

    /**
     * 修改人id：若是后台人员修改则填写此处
     */
    private Integer referUserId;

    /**
     * 运单状态修改记录时间
     */
    private Date createTime;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return waybill_id 
     */
    public Integer getWaybillId() {
        return waybillId;
    }

    /**
     * 
     * @param waybillId 
     */
    public void setWaybillId(Integer waybillId) {
        this.waybillId = waybillId;
    }

    /**
     * 跟踪类型：1-运输轨迹 2-补录轨迹
     * @return tracking_type 跟踪类型：1-运输轨迹 2-补录轨迹
     */
    public Integer getTrackingType() {
        return trackingType;
    }

    /**
     * 跟踪类型：1-运输轨迹 2-补录轨迹
     * @param trackingType 跟踪类型：1-运输轨迹 2-补录轨迹
     */
    public void setTrackingType(Integer trackingType) {
        this.trackingType = trackingType;
    }

    /**
     * 修改前状态
     * @return old_status 修改前状态
     */
    public String getOldStatus() {
        return oldStatus;
    }

    /**
     * 修改前状态
     * @param oldStatus 修改前状态
     */
    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus == null ? null : oldStatus.trim();
    }

    /**
     * 修改后状态
     * @return new_status 修改后状态
     */
    public String getNewStatus() {
        return newStatus;
    }

    /**
     * 修改后状态
     * @param newStatus 修改后状态
     */
    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus == null ? null : newStatus.trim();
    }

    /**
     * 司机id：若是APP操作修改，填写此处
     * @return driver_id 司机id：若是APP操作修改，填写此处
     */
    public Integer getDriverId() {
        return driverId;
    }

    /**
     * 司机id：若是APP操作修改，填写此处
     * @param driverId 司机id：若是APP操作修改，填写此处
     */
    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    /**
     * 纬度
     * @return latitude 纬度
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * 纬度
     * @param latitude 纬度
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * 经度
     * @return longitude 经度
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * 经度
     * @param longitude 经度
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * 轨迹描述
     * @return tracking_info 轨迹描述
     */
    public String getTrackingInfo() {
        return trackingInfo;
    }

    /**
     * 轨迹描述
     * @param trackingInfo 轨迹描述
     */
    public void setTrackingInfo(String trackingInfo) {
        this.trackingInfo = trackingInfo == null ? null : trackingInfo.trim();
    }

    /**
     * 发起修改设备序列号
     * @return device 发起修改设备序列号
     */
    public String getDevice() {
        return device;
    }

    /**
     * 发起修改设备序列号
     * @param device 发起修改设备序列号
     */
    public void setDevice(String device) {
        this.device = device == null ? null : device.trim();
    }

    /**
     * 修改人id：若是后台人员修改则填写此处
     * @return refer_user_id 修改人id：若是后台人员修改则填写此处
     */
    public Integer getReferUserId() {
        return referUserId;
    }

    /**
     * 修改人id：若是后台人员修改则填写此处
     * @param referUserId 修改人id：若是后台人员修改则填写此处
     */
    public void setReferUserId(Integer referUserId) {
        this.referUserId = referUserId;
    }

    /**
     * 运单状态修改记录时间
     * @return create_time 运单状态修改记录时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 运单状态修改记录时间
     * @param createTime 运单状态修改记录时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}