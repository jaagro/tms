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
public class WaybillTemp implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 需求车型id
     */
    private Integer needTruckType;

    /**
     * 创建时间
     */
    private Date createTime;
}