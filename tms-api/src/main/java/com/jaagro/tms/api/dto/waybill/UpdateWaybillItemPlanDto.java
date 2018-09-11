package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class UpdateWaybillItemPlanDto implements Serializable {

    /**
     * 订单明细id
     */
    private Integer itemId;

    /**
     * 订单货物列表
     */
    private List<UpdateWaybillGoodsPlanDto> goodsPlans;
}
