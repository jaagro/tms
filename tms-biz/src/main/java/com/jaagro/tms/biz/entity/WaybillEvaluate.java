package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class WaybillEvaluate {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 满意度 （1- 非常差 2- 一般 3-超级赞）
     */
    private Integer satisfactionLever;

    /**
     * 满意度描述
     */
    private String satisfactionLeverDesc;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建人 id
     */
    private Integer createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人id
     */
    private Integer modifyUserId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 
     */
    private Boolean enabled;

}