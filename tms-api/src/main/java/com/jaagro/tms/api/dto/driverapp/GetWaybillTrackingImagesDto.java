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
}
