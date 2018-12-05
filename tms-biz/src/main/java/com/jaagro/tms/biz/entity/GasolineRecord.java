package com.jaagro.tms.biz.entity;

import java.math.BigDecimal;
import java.util.Date;

public class GasolineRecord {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 车辆id
     */
    private Integer truckId;

    /**
     * 车队id
     */
    private Integer truckTeamId;

    /**
     * 车牌号码
     */
    private String truckNumber;

    /**
     * 加油公司名称
     */
    private String gasolineCompany;

    /**
     * 燃油类型
     */
    private String gasolineType;

    /**
     * 加油站名称
     */
    private String gasolineStationName;

    /**
     * 即时油价
     */
    private BigDecimal instantGasoline;

    /**
     * 加油升数
     */
    private BigDecimal gasolineLitre;

    /**
     * 加油金额
     */
    private BigDecimal gasolineAmount;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人id
     */
    private Integer createUserId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人
     */
    private Integer modifyUserId;

    /**
     * 备注
     */
    private String note;

    /**
     * 逻辑删除
     */
    private Boolean enabled;

    /**
     * 主键id
     * @return id 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 主键id
     * @param id 主键id
     */
    public void setId(Integer id) {
        this.id = id;
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
     * 加油公司名称
     * @return gasoline_company 加油公司名称
     */
    public String getGasolineCompany() {
        return gasolineCompany;
    }

    /**
     * 加油公司名称
     * @param gasolineCompany 加油公司名称
     */
    public void setGasolineCompany(String gasolineCompany) {
        this.gasolineCompany = gasolineCompany == null ? null : gasolineCompany.trim();
    }

    /**
     * 燃油类型
     * @return gasoline_type 燃油类型
     */
    public String getGasolineType() {
        return gasolineType;
    }

    /**
     * 燃油类型
     * @param gasolineType 燃油类型
     */
    public void setGasolineType(String gasolineType) {
        this.gasolineType = gasolineType == null ? null : gasolineType.trim();
    }

    /**
     * 加油站名称
     * @return gasoline_station_name 加油站名称
     */
    public String getGasolineStationName() {
        return gasolineStationName;
    }

    /**
     * 加油站名称
     * @param gasolineStationName 加油站名称
     */
    public void setGasolineStationName(String gasolineStationName) {
        this.gasolineStationName = gasolineStationName == null ? null : gasolineStationName.trim();
    }

    /**
     * 即时油价
     * @return instant_gasoline 即时油价
     */
    public BigDecimal getInstantGasoline() {
        return instantGasoline;
    }

    /**
     * 即时油价
     * @param instantGasoline 即时油价
     */
    public void setInstantGasoline(BigDecimal instantGasoline) {
        this.instantGasoline = instantGasoline;
    }

    /**
     * 加油升数
     * @return gasoline_litre 加油升数
     */
    public BigDecimal getGasolineLitre() {
        return gasolineLitre;
    }

    /**
     * 加油升数
     * @param gasolineLitre 加油升数
     */
    public void setGasolineLitre(BigDecimal gasolineLitre) {
        this.gasolineLitre = gasolineLitre;
    }

    /**
     * 加油金额
     * @return gasoline_amount 加油金额
     */
    public BigDecimal getGasolineAmount() {
        return gasolineAmount;
    }

    /**
     * 加油金额
     * @param gasolineAmount 加油金额
     */
    public void setGasolineAmount(BigDecimal gasolineAmount) {
        this.gasolineAmount = gasolineAmount;
    }

    /**
     * 支付方式
     * @return payment_method 支付方式
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * 支付方式
     * @param paymentMethod 支付方式
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod == null ? null : paymentMethod.trim();
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
     * 创建人id
     * @return create_user_id 创建人id
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 创建人id
     * @param createUserId 创建人id
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 修改时间
     * @return modify_time 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 修改时间
     * @param modifyTime 修改时间
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

    /**
     * 备注
     * @return note 备注
     */
    public String getNote() {
        return note;
    }

    /**
     * 备注
     * @param note 备注
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    /**
     * 逻辑删除
     * @return enabled 逻辑删除
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * 逻辑删除
     * @param enabled 逻辑删除
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}