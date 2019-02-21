package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @baiyiran
 */
@Data
@Accessors(chain = true)
public class CountUnFinishWaybillCriteriaDto implements Serializable {
    /**
     * 客户合同id
     */
    private Integer customerContractId;

    /**
     * 装货地id
     */
    private Integer loadSiteId;

    /**
     * 装货地id
     */
    private Integer unloadSiteId;

}
