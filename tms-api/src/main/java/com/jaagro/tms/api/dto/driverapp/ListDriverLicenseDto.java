package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class ListDriverLicenseDto implements Serializable {

    /**
     * 身份证号码
     */
    private String identityCard;

    /**
     * 驾驶证到期时间
     */
    private String expiryDrivingLicense;
    /**
     * 审验有效期止
     */
    private String validityInspection;
    /**
     * 下次清分日期
     */
    private String allocationTime;
    /**
     * 驾驶员帐号状态(0；未审核  1；审核未通过 2－停止合作，3－正常合作)
     */
    private Integer status;
    /**
     * 驾照类型(1 A1 2 B1 3 C1 4 C2)
     */
    private Integer drivingLicense;

}
