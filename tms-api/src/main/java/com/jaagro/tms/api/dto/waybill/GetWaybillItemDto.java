package com.jaagro.tms.api.dto.waybill;

import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class GetWaybillItemDto implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 卸货地id
     */
    private Integer unloadSiteId;

    /**
     * 卸货地对象
     */
    private ShowSiteDto showSiteDto;

    /**
     * 要求送达时间
     */
    private Date requiredTime;

    /**
     * 签收状态
     */
    private Boolean signStatus;

    /**
     * 合计重量
     */
    private BigDecimal totalWeight;

    /**
     * 合计数量
     */
    private Integer totalQuantity;

    /**
     * 货物列表
     */
    private List<GetWaybillGoodsDto> goods;
}
