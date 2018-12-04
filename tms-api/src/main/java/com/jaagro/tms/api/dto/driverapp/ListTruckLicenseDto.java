package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class ListTruckLicenseDto implements Serializable {

    /**
     * 车牌号码
     */
    private String truckNumber;
    /**
     * 购买日期
     */
    private Date buyTime;

    /**
     * 保险到期时间
     */
    private Date expiryDate;

    /**
     * 年检到期时间
     */
    private Date expiryAnnual;

    /**
     * 车辆状态(0；未审核  1；审核未通过 2－停止合作，3－正常合作)
     */
    private Integer truckStatus;
}
