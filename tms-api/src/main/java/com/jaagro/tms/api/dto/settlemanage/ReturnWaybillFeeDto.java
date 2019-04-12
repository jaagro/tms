package com.jaagro.tms.api.dto.settlemanage;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description: 结算管理运单费用
 * @author: @Gao.
 * @create: 2019-04-11 15:58
 **/
@Data
@Accessors(chain = true)
public class ReturnWaybillFeeDto implements Serializable {

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 客户id
     */
    private Integer customerId;
    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 车牌号id
     */
    private Integer truckId;

    /**
     * 车牌号
     */
    private String truckNumber;

    /**
     * 装货地id
     */
    private Integer loadSiteId;

    /**
     * 装货地名称
     */
    private String loadSiteName;

    /**
     * 装货时间
     */
    private Date loadSiteTime;

    /**
     * 装货地名称
     */
    private String unLoadSiteName;

    /**
     * 装货时间
     */
    private Date unLoadSiteTime;

    /**
     * 计划数量
     */
    private Integer totalQuantity;

    /**
     * 计划重量
     */
    private BigDecimal totalWeight;

    /**
     * 项目部id
     */
    private Integer netWorkId;

    /**
     * 项目部名称
     */
    private String netWorkName;

    /**
     * 客户费用
     */
    private BigDecimal customerFee;

    /**
     * 运力费用
     */
    private BigDecimal waybillFee;

    /**
     * 毛利
     */
    private BigDecimal grossProfit;

    /**
     * 运单商品信息
     */
    private List<WaybillGoodDto> waybillGoodDtos;
}
