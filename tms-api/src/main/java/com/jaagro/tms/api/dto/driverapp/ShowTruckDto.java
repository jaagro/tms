package com.jaagro.tms.api.dto.driverapp;

import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆信息
 * @author yj
 * @since 2018/12/11
 */
@Data
@Accessors(chain = true)
public class ShowTruckDto implements Serializable{
    /**
     * 主键车辆表ID
     */
    private Integer truckId;

    /**
     * 车牌号码
     */
    private String truckNumber;
    /**
     * 关联车队表ID
     */
    private Integer truckTeamId;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 司机名
     */
    private String driverName;

    /**
     * 是不是主驾
     */
    private Boolean mainDriver;

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
}
