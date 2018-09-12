package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author @gao
 */
@Data
@Accessors(chain = true)
public class GetWaybillTrackingImagesDto implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     *
     */
    private Integer waybillTrackingId;

    /**
     * 装货 卸货id
     */
    private Integer siteId;

    /**
     * 图片类型：1-装货单 2- 卸货单
     */
    private Integer imageType;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createUserId;

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
     *
     * @return waybill_tracking_id
     */
    public Integer getWaybillTrackingId() {
        return waybillTrackingId;
    }

    /**
     *
     * @param waybillTrackingId
     */
    public void setWaybillTrackingId(Integer waybillTrackingId) {
        this.waybillTrackingId = waybillTrackingId;
    }

    /**
     * 图片类型：1-装货单 2- 卸货单
     * @return image_type 图片类型：1-装货单 2- 卸货单
     */
    public Integer getImageType() {
        return imageType;
    }

    /**
     * 图片类型：1-装货单 2- 卸货单
     * @param imageType 图片类型：1-装货单 2- 卸货单
     */
    public void setImageType(Integer imageType) {
        this.imageType = imageType;
    }

    /**
     * 图片地址
     * @return image_url 图片地址
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 图片地址
     * @param imageUrl 图片地址
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
}
