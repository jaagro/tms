package com.jaagro.tms.api.dto.driverapp;

import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class ShowUnLoadSite implements Serializable {
    /**
     * waybillItemId
     */
    private Integer waybillItemId;
    /**
     * 卸货地信息
     */
    private ShowSiteDto showSiteDto;
}
