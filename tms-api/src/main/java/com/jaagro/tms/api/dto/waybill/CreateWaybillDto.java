package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
     * 需求车型id
     */
    private Integer needTruckTypeId;

    /**
     * 运单明细列表
     */
    private List<CreateWaybillItemsDto> waybillItems;
}
