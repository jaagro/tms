package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author @gao
 */
@Data
@Accessors(chain = true)
public class GetReceiptListAppDto implements Serializable {
    /**
     * 运单id
     */
    private Integer waybillId;
    /**
     * 提货时间
     */
    private Date loadTime;
    /**
     * 装货地
     */
    private ShowSiteAppDto loadSite;
    /**
     * 卸货地
     */
    private List<ShowSiteAppDto> unloadSite;

    /**
     * 货物
     */
    private List<ShowGoodsDto> goods;
}
