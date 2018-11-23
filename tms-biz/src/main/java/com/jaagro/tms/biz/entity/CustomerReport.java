package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class CustomerReport implements Serializable {
    /**
     * 客户报表id
     */
    private Integer id;

    /**
     * 统计时间(yyyy-mm-dd)
     */
    private Date reportTime;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 订单总数
     */
    private Integer orderQuantity;

    /**
     * 运单总数
     */
    private Integer waybillQuantity;

    /**
     * 异常单数
     */
    private Integer anomalyWaybillQuantity;

    /**
     * 货物类型
     */
    private Integer goodsType;

    /**
     * 数量(单位)
     */
    private Integer amount;

    /**
     * 吨位(单位)
     */
    private BigDecimal tonnage;

    /**
     * 收入-运费
     */
    private BigDecimal incomeFreight;

    /**
     * 收入-异常费用
     */
    private BigDecimal incomeAnomalyCost;

    /**
     * 成本-运费
     */
    private BigDecimal expendFreight;

    /**
     * 成本-异常费用
     */
    private BigDecimal expendAnomalyCost;

    /**
     * 毛利
     */
    private BigDecimal grossProfit;

    /**
     * 毛利率
     */
    private BigDecimal grossProfitRate;

    /**
     * 是否有效(0-无效,1-有效)
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date modifyTime;
}