package com.jaagro.tms.api.dto.waybill;

import com.jaagro.tms.api.dto.base.ShowTruckTypeDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author gavin
 */
@Data
@Accessors(chain = true)
public class ListWaybillPlanDto implements Serializable {
    /**
     *订单id
     */
    private Integer orderId;
    /**
     * 需求车型
     */
    private ShowTruckTypeDto needTruckType;
    /**
     * 装货地id
     */
    private Integer loadSiteId;
    /**
     * 装货时间(就是提货时间)
     */
    private Date loadTime;
    /**
     * 配置时间
     */
    private Date waybillPlanTime;
    /**
     * 车队合同id
     */
    private Integer truckTeamContractId;
    /**
     * 发货地和对应的货物明细列表 运单明细列表
   s  */
    private List<ListWaybillItemsPlanDto> waybillItems;
}
