package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class CreateGasolineRecordDto implements Serializable {

    /**
     * 车牌号码
     */
    private String truckNumber;

    /**
     * 加油公司名称
     */
    private String gasolineCompany;

    /**
     * 燃油类型
     */
    private String gasolineType;

    /**
     * 加油站名称
     */
    private String gasolineStationName;

    /**
     * 即时油价
     */
    private BigDecimal instantGasoline;

    /**
     * 加油升数
     */
    private BigDecimal gasolineLitre;

    /**
     * 加油金额
     */
    private BigDecimal gasolineAmount;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String note;

}
