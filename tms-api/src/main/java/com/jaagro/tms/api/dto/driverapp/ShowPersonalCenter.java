package com.jaagro.tms.api.dto.driverapp;

import com.jaagro.constant.UserInfo;
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
}
