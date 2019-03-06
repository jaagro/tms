package com.jaagro.tms.api.dto.fee;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author baiyiran
 * @date 2019/03/04
 */
@Data
@Accessors(chain = true)
public class ReturnWaybillCustomerFeeDto implements Serializable {

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 品名
     */
    private List<String> goodsNames;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 项目部id
     */
    private Integer departmentId;

    /**
     * 项目部
     */
    private String departmentName;

    /**
     * 货物类型
     */
    private Integer goodsType;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 运输费用
     */
    private BigDecimal waybillMoney;

    /**
     * 异常费用
     */
    private BigDecimal anomalyMoney;

    /**
     * 完成时间
     */
    private Date modifyTime;


}
