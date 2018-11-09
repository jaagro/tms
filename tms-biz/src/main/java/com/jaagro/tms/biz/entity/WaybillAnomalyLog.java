package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 运单异常日志
 * @author yj
 */
@Accessors(chain = true)
public class WaybillAnomalyLog implements Serializable{
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String oldStatus;

    /**
     * 
     */
    private String newStatus;

    /**
     * 
     */
    private String logInfo;

    /**
     * 
     */
    private Integer createUserId;

    /**
     * 
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
     * @return old_status 
     */
    public String getOldStatus() {
        return oldStatus;
    }

    /**
     * 
     * @param oldStatus 
     */
    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus == null ? null : oldStatus.trim();
    }

    /**
     * 
     * @return new_status 
     */
    public String getNewStatus() {
        return newStatus;
    }

    /**
     * 
     * @param newStatus 
     */
    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus == null ? null : newStatus.trim();
    }

    /**
     * 
     * @return log_info 
     */
    public String getLogInfo() {
        return logInfo;
    }

    /**
     * 
     * @param logInfo 
     */
    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo == null ? null : logInfo.trim();
    }

    /**
     * 
     * @return create_user_id 
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 
     * @param createUserId 
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 
     * @return create_time 
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 
     * @param createTime 
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}