package com.jaagro.tms.biz.entity;

import java.util.Date;

public class WashTruckRecord {
    /**
     * 洗车记录表id
     */
    private Integer id;

    /**
     * 车队id
     */
    private Integer truckTeamId;

    /**
     * 车辆id
     */
    private Integer truckId;

    /**
     * 车牌号码
     */
    private String truckNumber;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 司机名称
     */
    private String driverName;

    /**
     * 洗车地址
     */
    private String detailAddress;

    /**
     * 备注
     */
    private String notes;

    /**
     * 是否有效：1-有效 0-无效
     */
    private Boolean enable;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 修改人
     */
    private Integer modifyUserId;

    /**
     * 洗车记录表id
     * @return id 洗车记录表id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 洗车记录表id
     * @param id 洗车记录表id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 车队id
     * @return truck_team_id 车队id
     */
    public Integer getTruckTeamId() {
        return truckTeamId;
    }

    /**
     * 车队id
     * @param truckTeamId 车队id
     */
    public void setTruckTeamId(Integer truckTeamId) {
        this.truckTeamId = truckTeamId;
    }

    /**
     * 车辆id
     * @return truck_id 车辆id
     */
    public Integer getTruckId() {
        return truckId;
    }

    /**
     * 车辆id
     * @param truckId 车辆id
     */
    public void setTruckId(Integer truckId) {
        this.truckId = truckId;
    }

    /**
     * 车牌号码
     * @return truck_number 车牌号码
     */
    public String getTruckNumber() {
        return truckNumber;
    }

    /**
     * 车牌号码
     * @param truckNumber 车牌号码
     */
    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber == null ? null : truckNumber.trim();
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
     * 司机名称
     * @return driver_name 司机名称
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * 司机名称
     * @param driverName 司机名称
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName == null ? null : driverName.trim();
    }

    /**
     * 洗车地址
     * @return detail_address 洗车地址
     */
    public String getDetailAddress() {
        return detailAddress;
    }

    /**
     * 洗车地址
     * @param detailAddress 洗车地址
     */
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress == null ? null : detailAddress.trim();
    }

    /**
     * 备注
     * @return notes 备注
     */
    public String getNotes() {
        return notes;
    }

    /**
     * 备注
     * @param notes 备注
     */
    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }

    /**
     * 是否有效：1-有效 0-无效
     * @return enable 是否有效：1-有效 0-无效
     */
    public Boolean getEnable() {
        return enable;
    }

    /**
     * 是否有效：1-有效 0-无效
     * @param enable 是否有效：1-有效 0-无效
     */
    public void setEnable(Boolean enable) {
        this.enable = enable;
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

    /**
     * 创建人
     * @return create_user_id 创建人
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 创建人
     * @param createUserId 创建人
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 更新时间
     * @return modify_time 更新时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 更新时间
     * @param modifyTime 更新时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 修改人
     * @return modify_user_id 修改人
     */
    public Integer getModifyUserId() {
        return modifyUserId;
    }

    /**
     * 修改人
     * @param modifyUserId 修改人
     */
    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }
}