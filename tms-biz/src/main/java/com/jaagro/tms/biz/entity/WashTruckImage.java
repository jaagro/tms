package com.jaagro.tms.biz.entity;

import java.util.Date;

public class WashTruckImage {
    /**
     * 洗车图片表id
     */
    private Integer id;

    /**
     * 洗车记录id
     */
    private Integer washTruckRecordId;

    /**
     * 图片类型 1-车前,2-车中,3-车后
     */
    private Integer imageType;

    /**
     * 图片路径
     */
    private String imageUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private Integer createUserId;

    /**
     * 洗车图片表id
     * @return id 洗车图片表id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 洗车图片表id
     * @param id 洗车图片表id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 洗车记录id
     * @return wash_truck_record_id 洗车记录id
     */
    public Integer getWashTruckRecordId() {
        return washTruckRecordId;
    }

    /**
     * 洗车记录id
     * @param washTruckRecordId 洗车记录id
     */
    public void setWashTruckRecordId(Integer washTruckRecordId) {
        this.washTruckRecordId = washTruckRecordId;
    }

    /**
     * 图片类型 1-车前,2-车中,3-车后
     * @return image_type 图片类型 1-车前,2-车中,3-车后
     */
    public Integer getImageType() {
        return imageType;
    }

    /**
     * 图片类型 1-车前,2-车中,3-车后
     * @param imageType 图片类型 1-车前,2-车中,3-车后
     */
    public void setImageType(Integer imageType) {
        this.imageType = imageType;
    }

    /**
     * 图片路径
     * @return image_url 图片路径
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 图片路径
     * @param imageUrl 图片路径
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
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
     * 创建时间
     * @return create_user_id 创建时间
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 创建时间
     * @param createUserId 创建时间
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }
}