package com.jaagro.tms.biz.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
     * 设备类型 1-手机 2-车载GPS
     * @return device_type 设备类型 1-手机 2-车载GPS
     */
    public Integer getDeviceType() {
        return deviceType;
    }

    /**
     * 设备类型 1-手机 2-车载GPS
     * @param deviceType 设备类型 1-手机 2-车载GPS
     */
    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 设备信息(IMEI等)
     * @return device_info 设备信息(IMEI等)
     */
    public String getDeviceInfo() {
        return deviceInfo;
    }

    /**
     * 设备信息(IMEI等)
     * @param deviceInfo 设备信息(IMEI等)
     */
    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo == null ? null : deviceInfo.trim();
    }

    /**
     * 司机id
     * @return driver_id 司机id
     */
    public Integer getDriverId() {
        return driverId;
    }

    /**
     * 司机id
     * @param driverId 司机id
     */
    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    /**
     * 车辆ID
     * @return truck_id 车辆ID
     */
    public Integer getTruckId() {
        return truckId;
    }

    /**
     * 车辆ID
     * @param truckId 车辆ID
     */
    public void setTruckId(Integer truckId) {
        this.truckId = truckId;
    }

    /**
     * 运单id
     * @return waybill_id 运单id
     */
    public Integer getWaybillId() {
        return waybillId;
    }

    /**
     * 运单id
     * @param waybillId 运单id
     */
    public void setWaybillId(Integer waybillId) {
        this.waybillId = waybillId;
    }

    /**
     * 运单状态
     * @return waybill_status 运单状态
     */
    public String getWaybillStatus() {
        return waybillStatus;
    }

    /**
     * 运单状态
     * @param waybillStatus 运单状态
     */
    public void setWaybillStatus(String waybillStatus) {
        this.waybillStatus = waybillStatus == null ? null : waybillStatus.trim();
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
     * 速度
     * @return speed 速度
     */
    public BigDecimal getSpeed() {
        return speed;
    }

    /**
     * 速度
     * @param speed 速度
     */
    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    /**
     * 角度
     * @return angle 角度
     */
    public BigDecimal getAngle() {
        return angle;
    }

    /**
     * 角度
     * @param angle 角度
     */
    public void setAngle(BigDecimal angle) {
        this.angle = angle;
    }

    /**
     * 设备定位的时间
     * @return location_time 设备定位的时间
     */
    public Date getLocationTime() {
        return locationTime;
    }

    /**
     * 设备定位的时间
     * @param locationTime 设备定位的时间
     */
    public void setLocationTime(Date locationTime) {
        this.locationTime = locationTime;
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}