package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gavin
 *
 * @Datetime 20190107
 */
@Data
@Accessors(chain = true)
public class ImportWaybillDto implements Serializable {

    /**
     * 车牌号码
     */
    private String truckNumber;

    /**
     * 装货时间(就是提货时间)
     */
    private Date loadTime;

    /**
     * 要求送达时间 (就是卸货时间)
     */
    private Date requiredTime;

    /**
     * 计划数量
     */
    private Integer goodsQuantity;
}
