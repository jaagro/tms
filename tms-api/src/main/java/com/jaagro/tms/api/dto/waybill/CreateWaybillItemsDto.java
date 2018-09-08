package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class CreateWaybillItemsDto implements Serializable {

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 卸货地id
     */
    private Integer unloadSiteId;

    /**
     * 要求送达时间
     */
    private Date requiredTime;

    /**
     * 商品信息列表
     */
    private List<CreateWaybillGoodsDto> goods;
}
