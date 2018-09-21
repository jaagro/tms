package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class SignProductDto implements Serializable{

    /**
     * 卸货地id
     */
    private Integer unLoadSiteId;
    /**
     *
     */
    private Integer waybillItemId;
    /**
     *
     */
}
