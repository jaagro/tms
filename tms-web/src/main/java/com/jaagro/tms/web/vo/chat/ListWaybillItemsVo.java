package com.jaagro.tms.web.vo.chat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author baiyiran
 * @Date 2018/11/5
 */
@Data
@Accessors(chain = true)
public class ListWaybillItemsVo implements Serializable {
    /**
     * 卸货地对象
     */
    private ShowSiteVo uploadSiteVo;

    /**
     * 要求送达时间
     */
    private Date requiredTime;

    /**
     * 签收状态
     */
    private Boolean signStatus;
    /**
     * 货物列表
     */
    private List<ListWaybillGoodsVo> goods;
}
