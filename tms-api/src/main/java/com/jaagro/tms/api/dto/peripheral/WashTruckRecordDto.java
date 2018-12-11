package com.jaagro.tms.api.dto.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author yj
 * @since 2018/12/11
 */
@Data
@Accessors(chain = true)
public class WashTruckRecordDto implements Serializable {
    /**
     * 洗车记录表id
     */
    private Integer id;

    /**
     * 车队id
     */
    private Integer truckTeamId;

    /**
     * 车辆id
     */
    private Integer truckId;

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
     * 是否有效：1-有效 0-无效
     */
    private Boolean enable;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 修改人
     */
    private Integer modifyUserId;

    /**
     * 洗车图片
     */
    private List<WashTruckImageDto> washTruckImageDtoList;
}
