package com.jaagro.tms.web.vo.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class GasolineRecordListVo implements Serializable {


    /**
     * 加油公司名称
     */
    private String gasolineCompany;


    /**
     * 即时油价
     */
    private BigDecimal instantGasoline;


    /**
     * 创建时间
     */
    private Date createTime;
}
