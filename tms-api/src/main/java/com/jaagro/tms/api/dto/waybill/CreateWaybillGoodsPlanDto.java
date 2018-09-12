package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author gavin
 */
@Data
@Accessors(chain = true)
public class CreateWaybillGoodsPlanDto implements Serializable {
    /**
     *货物id
     */
    private Integer goodsId;
    /**
     * 可配量
     */
    private Integer proportioning;
}
