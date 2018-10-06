package com.jaagro.tms.biz.entity;

public class CatchChickenTime {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer farmsType;

    /**
     * 
     */
    private Integer truckTypeId;

    /**
     * 
     */
    private Integer catchTime;

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
     * @return farms_type 
     */
    public Integer getFarmsType() {
        return farmsType;
    }

    /**
     * 
     * @param farmsType 
     */
    public void setFarmsType(Integer farmsType) {
        this.farmsType = farmsType;
    }

    /**
     * 
     * @return truck_type_id 
     */
    public Integer getTruckTypeId() {
        return truckTypeId;
    }

    /**
     * 
     * @param truckTypeId 
     */
    public void setTruckTypeId(Integer truckTypeId) {
        this.truckTypeId = truckTypeId;
    }

    /**
     * 
     * @return catch_time 
     */
    public Integer getCatchTime() {
        return catchTime;
    }

    /**
     * 
     * @param catchTime 
     */
    public void setCatchTime(Integer catchTime) {
        this.catchTime = catchTime;
    }
}