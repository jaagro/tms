package com.jaagro.tms.api.dto.truck;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class DriverReturnDto implements Serializable {
    /**
     * 主键司机ID
     */
    private Integer id;

    /**
     * 关联车队表ID
     */
    private Integer truckTeamId;

    /**
     * 关联车辆ID
     */
    private Integer truckId;

    /**
     * 司机姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String identityCard;

    /**
     * 驾照类型(1 A1 2 B1 3 C1 4 C2)
     */
    private Integer drivingLicense;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 密码加盐
     */
    private String salt;

    /**
     * 驾驶证到期时间
     */
    private String expiryDrivingLicense;

    /**
     * 驾驶员帐号状态(0；未审核  1；审核未通过 2－停止合作，3－正常合作)
     */
    private Integer status;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 修改日期
     */
    private Date modifyTime;

    /**
     * 创建人(关联用户ID)
     */
    private Integer createUserId;

    /**
     * 修改人(关联用户ID)
     */
    private Integer modifyUserId;

    /**
     * 备注
     */
    private String notes;

    /**
     * 所属城市
     */
    private String city;

    /**
     * 所属省份
     */
    private String province;

    /**
     * 所属区县
     */
    private String county;

    /**
     * 设备标识，用于jpush
     */
    private String registrationId;
}
