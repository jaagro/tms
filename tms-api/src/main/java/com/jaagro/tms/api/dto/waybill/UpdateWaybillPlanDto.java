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
public class UpdateWaybillPlanDto implements Serializable {

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单明细列表
     */
    private List<UpdateWaybillItemPlanDto> waybillItemPlans;
}
