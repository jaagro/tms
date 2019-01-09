package com.jaagro.tms.api.dto.truck;

import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liqiangping
 */
@Data
@Accessors(chain = true)
public class GetTruckDto implements Serializable {
    /**
     * 主键车辆表ID
     */
    private Integer id;

    /**
     * 关联车队表ID
     */
    private Integer truckTeamId;

    /**
     * 车牌号码
     */
    private String truckNumber;

    /**
     * 车辆品牌
     */
    private String vehicleBrand;

    /**
     * 关联车辆类型ID
     */
    private ListTruckTypeDto truckTypeId;

    /**
     * 最大载重
     */
    private BigDecimal capacity;

    /**
     * 装载数量
     */
    private Integer maximumLoad;

    /**
     * 车辆自重
     */
    private BigDecimal truckWeight;

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
     * 营运证号
     */
    private String businessPermit;

    /**
     * 行驶证号
     */
    private String drivingPermit;

    /**
     * 备注信息
     */
    private String notes;

    /**
     * 车辆状态(0；未审核  1；审核未通过 2－停止合作，3－正常合作)
     */
    private Integer truckStatus;

    /**
     * 修改日期
     */
    private Date modifyTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人(关联用户ID)
     */
    private Integer modifyUserId;

    /**
     * 创建人(关联用户ID)
     */
    private Integer createUserId;

    /**
     * 百公里油耗
     */
    private BigDecimal fuelConsumption;

    /**
     * 车辆类型
     */
    private Integer vehicleType;

    /**
     * 司机列表
     */
    private List<DriverReturnDto> drivers;

    /**
     * 车辆资质列表
     */
    private List<ListTruckQualificationDto> qualificationDtoList;
}
