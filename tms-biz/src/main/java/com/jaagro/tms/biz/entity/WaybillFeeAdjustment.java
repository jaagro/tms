package com.jaagro.tms.biz.entity;

import com.sun.xml.internal.ws.encoding.soap.SerializerConstants;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class WaybillFeeAdjustment implements SerializerConstants {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer waybillId;

    /**
     * 
     */
    private Integer waybillItemId;

    /**
     * 调整原因类型：1-卸货费  2-货损费。。。
     */
    private Integer adjustReason;

    /**
     * 调整类型：1-货主向司机付费，2-司机向货主付费
     */
    private Integer adjustType;

    /**
     * 是否对客户有效
     */
    private Boolean customerEffect;

    /**
     * 是否对司机有效
     */
    private Boolean truckEffect;

    /**
     * 备注信息
     */
    private String notes;

    /**
     * 是否有效
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createdUserId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人id
     */
    private Integer modifyUserId;
}