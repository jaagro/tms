package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class WaybillItems implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 卸货地id
     */
    private Integer unloadSiteId;

    /**
     * 要求送达时间
     */
    private Date requiredTime;

    /**
     * 是否有效
     */
    private Boolean enabled;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人id
     */
    private Integer modifyUserId;

    /**
     * 签收状态
     */
    private Boolean signStatus;
}