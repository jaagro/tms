package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author baiyiran
 * @date 2019/1/10
 */
@Data
@Accessors(chain = true)
public class GrabWaybillRecord implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 货车id
     */
    private Integer truckId;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     *
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人id
     */
    private Integer createUserId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人的id
     */
    private Integer modifyUserId;

    /**
     * 逻辑删除
     */
    private Boolean enabled;
}