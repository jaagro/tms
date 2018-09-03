package com.jaagro.tms.api.dto.truck;

import com.jaagro.tms.api.dto.base.ShowTruckTypeDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
     * 车辆品牌
     */
    private String vehicleBrand;

    /**
     * 关联车辆类型ID
     */
    private ShowTruckTypeDto truckType;

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
}
