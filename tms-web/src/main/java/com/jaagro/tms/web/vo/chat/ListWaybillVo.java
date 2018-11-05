package com.jaagro.tms.web.vo.chat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyiran
 * 微信小程序运单列表vo
 */
@Data
@Accessors(chain = true)
public class ListWaybillVo {
    /**
     *
     */
    private Integer id;
    /**
     * 货物类型
     */
    private Integer goodType;
    /**
     * 装货时间
     */
    private Date loadTime;
    /**
     * 装货地
     */
    private ShowSiteVo loadSiteVo;

    /**
     * 运单明细list
     */
    private List<ListWaybillItemsVo> waybillItemsVoList;
}
