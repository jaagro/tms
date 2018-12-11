package com.jaagro.tms.biz.entity;

import java.util.Date;

public class WaybillEvaluate {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 满意度 （1- 非常差 2- 一般 3-超级赞）
     */
    private Integer satisfactionLever;

    /**
     * 满意度描述
     */
    private String satisfactionLeverDesc;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建人 id
     */
    private Integer createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人id
     */
    private Integer modifyUserId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 
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
     * 满意度 （1- 非常差 2- 一般 3-超级赞）
     * @return satisfaction_lever 满意度 （1- 非常差 2- 一般 3-超级赞）
     */
    public Integer getSatisfactionLever() {
        return satisfactionLever;
    }

    /**
     * 满意度 （1- 非常差 2- 一般 3-超级赞）
     * @param satisfactionLever 满意度 （1- 非常差 2- 一般 3-超级赞）
     */
    public void setSatisfactionLever(Integer satisfactionLever) {
        this.satisfactionLever = satisfactionLever;
    }

    /**
     * 满意度描述
     * @return satisfaction_lever_desc 满意度描述
     */
    public String getSatisfactionLeverDesc() {
        return satisfactionLeverDesc;
    }

    /**
     * 满意度描述
     * @param satisfactionLeverDesc 满意度描述
     */
    public void setSatisfactionLeverDesc(String satisfactionLeverDesc) {
        this.satisfactionLeverDesc = satisfactionLeverDesc == null ? null : satisfactionLeverDesc.trim();
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
     * 创建人 id
     * @return create_user_id 创建人 id
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 创建人 id
     * @param createUserId 创建人 id
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
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
     * 修改人id
     * @return modify_user_id 修改人id
     */
    public Integer getModifyUserId() {
        return modifyUserId;
    }

    /**
     * 修改人id
     * @param modifyUserId 修改人id
     */
    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
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
     * 
     * @return enabled 
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * 
     * @param enabled 
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}