package com.jaagro.tms.api.dto.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 维修记录Dto
 */
@Data
@Accessors(chain = true)
public class RepairRecordDto implements Serializable {

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
}
