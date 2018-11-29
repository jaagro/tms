package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class WaybillTrackingImages {
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
    private Integer waybillItemId;

    /**
     * 
     */
    private Integer siteId;

    /**
     * 
     */
    private Integer waybillTrackingId;

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
     * 区分装卸货地 1--装货地 2--卸货地
     */
    private Integer type;


}