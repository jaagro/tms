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
public class CreateWaybillItemsPlanDto implements Serializable {

    /**
     * 订单明细id
     */
    private Integer orderItemId;

    /**
     * 商品列表
     */
    private List<CreateWaybillGoodsPlanDto> goods;
}
