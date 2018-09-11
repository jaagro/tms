package com.jaagro.tms.api.dto.waybill;

import com.jaagro.tms.api.dto.customer.ShowSiteDto;
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
     * 订单明细id 用于获取卸货地id
     */
    private Integer orderItemId;
    /**
     * 卸货地对象
     */
    private ShowSiteDto showSiteDto;
    /**
     * 要求送达时间
     */
    private Date requiredTime;

    /**
     * 货物列表
     */
    private List<ListWaybillGoodsPlanDto> goods;
}
