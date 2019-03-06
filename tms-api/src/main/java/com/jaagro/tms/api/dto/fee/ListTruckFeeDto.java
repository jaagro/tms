package com.jaagro.tms.api.dto.fee;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 司机运力费用
 * @author yj
 * @date 2019/3/4 14:59
 */
@Data
@Accessors(chain = true)
public class ListTruckFeeDto implements Serializable{
    private static final long serialVersionUID = -5583974185313804894L;
    /**
     * 运单号
     */
    private Integer waybillId;
    /**
     * 完成时间
     */
    private String completeTime;
    /**
     * 货物名称
     */
    private String goodsName;
    /**
     * 货物数量
     */
    private Integer unloadQuantity;
    /**
     * 货物重量
     */
    private BigDecimal unloadWeight;
    /**
     * 货物类型(1-毛鸡,2-饲料,3-母猪,4-公猪,5-仔猪,6-生猪)
     */
    private Integer goodsType;
    /**
     * 项目部名称
     */
    private String departmentName;
    /**
     * 车牌号
     */
    private String truckNumber;
    /**
     * 装货地名称
     */
    private String loadSite;
    /**
     * 卸货地名称
     */
    private String unloadSite;
    /**
     * 运输费用
     */
    private BigDecimal transportFee;
    /**
     * 异常费用
     */
    private BigDecimal anomalyFee;
    /**
     * 总费用
     */
    private BigDecimal totalFee;
}
