package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Gavin
 */
@Data
@Accessors(chain = true)
public class ListWaybillItemsPlanDto implements Serializable {
    /**
     * 卸货地id
     */
    private Integer unloadSiteId;
    /**
     * 要求送达时间
     */
    private Date requiredTime;

    /**
     * 货物列表
     */
    private List<ListWaybillGoodsPlanDto> goods;
}
