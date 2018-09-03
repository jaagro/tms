package com.jaagro.tms.api.dto.waybill;

import com.jaagro.tms.api.dto.base.ShowUserDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class GetWaybillGoodsDto implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer waybillItemId;

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
     * 是否加药
     */
    private Boolean joinDrug;

    /**
     * 是否有效
     */
    private Boolean enabled;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人id
     */
    private ShowUserDto modifyUserId;
}
