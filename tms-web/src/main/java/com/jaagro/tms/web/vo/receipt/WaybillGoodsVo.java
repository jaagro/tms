package com.jaagro.tms.web.vo.receipt;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 运单货物信息
 * @author yj
 * @date 2018/10/31
 */
@Data
@Accessors(chain = true)
public class WaybillGoodsVo implements Serializable{
    /**
     * 运单货物iid
     */
    private Integer id;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 运单卸货地id
     */
    private Integer waybillItemId;

    /**
     * 卸货地id
     */
    private Integer unloadSiteId;

    /**
     * 货物名称
     */
    private String goodsName;

    /**
     * 货物单位：1-羽 2-头 3-吨
     */
    private Integer goodsUnit;
    /**
     * 计划数量
     */
    private Integer goodsQuantity;

    /**
     * 计划重量
     */
    private BigDecimal goodsWeight;

    /**
     * 装货数量
     */
    private Integer loadQuantity;

    /**
     * 装货重量
     */
    private BigDecimal loadWeight;

    /**
     * 卸货数量
     */
    private Integer unloadQuantity;

    /**
     * 卸货重量
     */
    private BigDecimal unloadWeight;

    /**
     * 要求送达时间
     */
    private Date requiredTime;

    /**
     * 是否加药
     */
    private Boolean joinDrug;

    /**
     * 卸货地名称
     */
    private String unloadSiteName;

}
