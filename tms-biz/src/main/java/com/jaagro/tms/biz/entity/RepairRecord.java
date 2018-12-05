package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class RepairRecord {
    /**
     * 
     */
    private Integer id;

    /**
     * 车辆id
     */
    private Integer truckId;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 车牌号码
     */
    private String truckNumber;

    /**
     * 维修项目
     */
    private String repairItem;

    /**
     * 进厂日期
     */
    private Date inDate;

    /**
     * 计划完工日期
     */
    private Date finishDate;

    /**
     * 维修地址
     */
    private String repairAddress;

    /**
     * 维修详细描述
     */
    private String description;

    /**
     * 是否有效
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 修改人id
     */
    private Integer modifyUserId;

    /**
     * 车队id
     */
    private Integer truckTeamId;

}