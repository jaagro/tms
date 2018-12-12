package com.jaagro.tms.web.vo.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 洗车记录
 * @author yj
 * @since 2018/12/11
 */
@Data
@Accessors(chain = true)
public class WashTruckRecordVo implements Serializable{
    /**
     * 洗车记录表id
     */
    private Integer id;

    /**
     * 车牌号码
     */
    private String truckNumber;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 司机名称
     */
    private String driverName;

    /**
     * 洗车地址
     */
    private String detailAddress;

    /**
     * 备注
     */
    private String notes;

    /**
     * 创建时间
     */
    private Date createTime;
}
