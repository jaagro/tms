package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author gavin
 */
@Data
@Accessors(chain = true)
public class ShowLocationDto implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;


    /**
     * 设备定位的时间
     */
    private Date locationTime;


}