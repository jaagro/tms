package com.jaagro.tms.api.dto.truck;

import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.base.ShowTruckTypeDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ShowTruckDto implements Serializable {

    /**
     * 主键车辆表ID
     */
    private Integer id;

    /**
     * 车牌号码
     */
    private String truckNumber;
    /**
     * 关联车队表ID
     */
    private Integer truckTeamId;

    /**
     * 关联车辆类型ID
     */
    private ListTruckTypeDto truckTypeId;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区县
     */
    private String county;

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

    /**
     * 司机列表
     */
    private List<ShowDriverDto> drivers;
}
