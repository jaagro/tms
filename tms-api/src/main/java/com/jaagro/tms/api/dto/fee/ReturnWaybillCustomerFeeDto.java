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
     *
     */
    private Integer id;

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
     * 项目部
     */
    private String departName;

    /**
     * 货物类型
     */
    private Integer goodsType;

    /**
     * 货物类型
     */
    private Integer quantity;

    /**
     * 异常id
     */
    private Integer anomalyId;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 完成时间
     */
    private Date modifyTime;


}
