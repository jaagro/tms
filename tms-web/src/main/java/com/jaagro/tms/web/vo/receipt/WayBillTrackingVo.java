package com.jaagro.tms.web.vo.receipt;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 运单轨迹(补录)
 * @author yj
 * @date 2018/10/31
 */
@Data
@Accessors(chain = true)
public class WayBillTrackingVo implements Serializable{

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 补录人
     */
    private String operator;

}
