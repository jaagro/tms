package com.jaagro.tms.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tony
 */
@Data
public class  LoadExcelDto implements Serializable {

    /**
     * 相同则需要合并
     */
    private Integer code;

    /**
     * 货物类型
     */
    private String productType;

    /**
     * 运输类型
     */
    private String transportationType;

    /**
     * 客户
     */
    private String customer;

    /**
     * 货物名称
     */
    private String productName;

    /**
     * 件数
     */
    private Integer quantity;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 里程数
     */
    private BigDecimal mileage;

    /**
     * 收货人
     */
    private String shippingAddress;

    /**
     * 回单数量
     */
    private Integer receiptCount;

    /**
     * 要求送达时间
     */
    private Date requiredDeliveryDatetime;

    /**
     * 计价方式
     */
    private String pricingMethod;

    /**
     * 运费单价
     */
    private BigDecimal shippingUnitPrice;

    /**
     * 运费小计
     */
    private BigDecimal shippingCostTotal;

    /**
     * 结算方式
     */
    private String paymentType;

    /**
     * 要求提货时间
     */
    private Date requiredPickupDatetime;

    /**
     * 备注
     */
    private String notes;

    /**
     * 是否加药
     */
    private boolean appliedMedicine;

    /**
     * 装车位置
     */
    private String loadingPosition;

    /**
     * 承运日期
     */
    private Date acceptedDate;
}
