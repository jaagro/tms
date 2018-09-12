package com.jaagro.tms.api.dto.order;

import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.api.dto.driverapp.ShowSiteAppDto;
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
public class GetOrderItemsDto implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 卸货地id
     */
    private ShowSiteAppDto unload;

    /**
     * 要求卸货时间
     */
    private Date unloadTime;

    /**
     * 是否有效
     */
    private Boolean enabled;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人id
     */
    private ShowUserDto modifyUserId;

    /**
     * 货物列表
     */
    private List<GetOrderGoodsDto> goods;
}
