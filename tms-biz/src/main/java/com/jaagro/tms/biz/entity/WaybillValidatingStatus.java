package com.jaagro.tms.biz.entity;

public class WaybillValidatingStatus {
    private Long waybillValidatingStatusId;

    private String name;

    private Boolean isDeleted;

    private Integer code;

    public Long getWaybillValidatingStatusId() {
        return waybillValidatingStatusId;
    }

    public void setWaybillValidatingStatusId(Long waybillValidatingStatusId) {
        this.waybillValidatingStatusId = waybillValidatingStatusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}