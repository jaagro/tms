package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author gavin
 */
@Data
@Accessors(chain = true)
public class CreateWaybillDto implements Serializable {

    /**
     *订单id
     */
    private Integer orderId;

    /**
     * 装货地id
     */
    private Integer loadSiteId;

    /**
     * 车队合同id
     */
    private Integer truckTeamContractId;

    /**
     * 需求车型
     */
    private Integer needTruckType;

    /**
     * 实际公里数
     */
    private BigDecimal distance;
    /**
     * 发货地和对应的货物明细列表 运单明细列表
     */
    private List<CreateWaybillItemsDto> waybillItems;

}
