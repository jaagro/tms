package com.jaagro.tms.api.dto.order;

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
public class GetOrderGoodsDto implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer orderItemId;

    /**
     * 货物名称
     */
    private String goodsName;

    /**
     * 货物单位：1-羽 2-头 3-吨
     */
    private Integer goodsUnit;

    /**
     * 货物数量
     */
    private Integer goodsQuantity;

    /**
     * 货物重量
     */
    private BigDecimal goodsWeight;

    /**
     * 余量
     */
    private BigDecimal margin;

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
    private ShowUserDto modifyUser;
}
