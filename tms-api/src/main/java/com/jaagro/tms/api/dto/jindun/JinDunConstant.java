package com.jaagro.tms.api.dto.jindun;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @description: 金盾gsp常量
 * @author: @Gao.
 * @create: 2019-04-24 09:52
 **/
@Data
@Accessors(chain = true)
public class JinDunConstant implements Serializable {
    /**
     * 金盾登录账户
     */

    public static final String ACCESS_NAME = "sqjd1";
    /**
     * 金盾登录密码
     */
    public static final String ACCESS_PASSWORD = "123456";

    /**
     *
     */
    public static final String JSESSION = "JSESSION_TO_REDIS";

    /**
     * 登录url
     */
    public static final String LOGIN_URL = "http://120.78.214.191/StandardApiAction_login.action";

    /**
     * 获取车辆历史轨迹
     */
    public static final String TRACK_URL = "http://120.78.214.191/StandardApiAction_queryTrackDetail.action";

}
