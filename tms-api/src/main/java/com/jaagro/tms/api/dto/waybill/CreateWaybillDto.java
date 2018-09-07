package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class CreateWaybillDto implements Serializable {

    /**
     *
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
     * 运单明细列表
     */
    private List<CreateWaybillItemsDto> waybillItems;
}
