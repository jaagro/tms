package com.jaagro.tms.api.dto.waybill;

import com.jaagro.tms.api.dto.order.GetOrderDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class GetWaybillPlanDto implements Serializable {

    /**
     * 订单信息
     */
    private GetOrderDto orderDto;

    /**
     * 订单相关运单列表
     */
//    private List<GetWaybillDto> waybillDtoList;
}
