package com.jaagro.tms.api.dto.truck;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ShowDriverDto implements Serializable {

    private Integer id;

    /**
     * 司机名
     */
    private String name;

    /**
     * 是不是主驾
     */
    private Boolean maindriver;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 身份证号码
     */
    private String identityCard;

    /**
     * 驾照类型(1 A1 2 B1 3 C1 4 C2)
     */
    private Integer drivingLicense;

    /**
     * 驾驶证到期时间
     */
    private String expiryDrivingLicense;

    /**
     * 驾驶员帐号状态(0；未审核  1；审核未通过 2－停止合作，3－正常合作)
     */
    private Integer status;
    /**
     * 设备标识，用于jpush
     */
    private String registrationId;
}
