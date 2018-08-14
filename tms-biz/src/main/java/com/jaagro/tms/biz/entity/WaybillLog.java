package com.jaagro.tms.biz.entity;

import java.util.Date;

public class WaybillLog {
    private Long waybillLogId;

    private Long targetWaybillId;

    private Long operatedUserId;

    private Date operatedDate;

    private String operationDescription;

    private Boolean isOperationLog;

    private Boolean isDeleted;

    public Long getWaybillLogId() {
        return waybillLogId;
    }

    public void setWaybillLogId(Long waybillLogId) {
        this.waybillLogId = waybillLogId;
    }

    public Long getTargetWaybillId() {
        return targetWaybillId;
    }

    public void setTargetWaybillId(Long targetWaybillId) {
        this.targetWaybillId = targetWaybillId;
    }

    public Long getOperatedUserId() {
        return operatedUserId;
    }

    public void setOperatedUserId(Long operatedUserId) {
        this.operatedUserId = operatedUserId;
    }

    public Date getOperatedDate() {
        return operatedDate;
    }

    public void setOperatedDate(Date operatedDate) {
        this.operatedDate = operatedDate;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription == null ? null : operationDescription.trim();
    }

    public Boolean getIsOperationLog() {
        return isOperationLog;
    }

    public void setIsOperationLog(Boolean isOperationLog) {
        this.isOperationLog = isOperationLog;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}