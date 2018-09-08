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
public class CreateWaybillItemsDto implements Serializable {

    /**
     * 卸货地id
     */
    private Integer unloadSiteId;


    /**
     * 货物列表
     */
    private List<CreateWaybillGoodsDto> goods;
}
