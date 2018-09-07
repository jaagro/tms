package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 配载计划入参dto
 * @author tony
 */
@Data
@Accessors(chain = true)
public class CreateWaybillPlanDto implements Serializable {

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 车型数组
     */
    private int[] truckTypes;

    /**
     * 订单明细列表
     */
    private List<CreateWaybillItemsPlanDto> items;
}
