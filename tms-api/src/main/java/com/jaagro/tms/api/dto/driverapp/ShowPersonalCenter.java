package com.jaagro.tms.api.dto.driverapp;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.account.AccountReturnDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class ShowPersonalCenter implements Serializable {

    /**
     * 用户个人信息
     */
    private UserInfo userInfo;
    /**
     * 账户信息 add by yj 20181112
     */
    private AccountReturnDto accountInfo;
    /**
     * 我的驾照
     */
    private ListDriverLicenseDto driverLicenseDto;
    /**
     * 车辆证件信息
     */
    private ListTruckLicenseDto truckLicenseDto;
    /**
     * 车辆信息
     */
    private ShowTruckDto truckInfo;

}
