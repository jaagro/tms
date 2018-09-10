package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author Gavin
 */
@Data
@Accessors(chain = true)
public class CreateWaybillItemsPlanDto implements Serializable {

   /**
     * 订单明细id 用于获取卸货地id
     */
    private Integer orderItemId;

    /**
     * 货物列表
     */
    private List<CreateWaybillGoodsPlanDto> goods;
}
