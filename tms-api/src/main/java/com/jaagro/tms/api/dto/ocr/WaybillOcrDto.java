package com.jaagro.tms.api.dto.ocr;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author tonyZheng
 * @date 2019-01-15 09:38
 */
@Data
public class WaybillOcrDto implements Serializable {

    /** 运单id */
    private int waybillId;

    /** 货物总条数 */
    private int goodsCount;

    /**
     * 卸货地
     */
    private String unLoadSite;

    /** 货物明细 */
    private List<BigDecimal> goodsItems;
}
