package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class GetWaybillTruckingParamDto implements Serializable {
    /**
     * 运单状态
     */
    private String waybillStatus;
    /**
     * 运单id
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
    /**
     * 货车id
     */
    private Integer truckId;
    /**
     * 确认提货信息
     */
    private List<ConfirmProductDto> confirmProductDtos;
    /**
     *是否显示提货信息列表
     */
    private String showStatus;
    /**
     * 提货单URL
     */
    private List<String> imagesUrl;

}
