package com.jaagro.tms.biz.entity;

public class WaybillStatus {
    private Long waybillStatusId;

    private String name;

    private Boolean isDeleted;

    private Integer code;

    public Long getWaybillStatusId() {
        return waybillStatusId;
    }

    public void setWaybillStatusId(Long waybillStatusId) {
        this.waybillStatusId = waybillStatusId;
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