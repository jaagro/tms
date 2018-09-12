package com.jaagro.tms.api.dto.waybill.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author @gao
 */
@Data
@Accessors(chain = true)
public class GetReceiptParamDto implements Serializable {

    /**
     * 起始页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 运单
     */
    private Integer waybillId;
    /**
     * 发起修改设备序列号
     */
    private String device;
    /**
     * 轨迹描述
     */
    private String trackingInfo;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 是否接单
     */
    private String receiptStatus;

}
