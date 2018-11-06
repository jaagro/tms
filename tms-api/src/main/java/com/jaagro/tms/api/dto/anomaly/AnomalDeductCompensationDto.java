package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author @Gao.
 * 是否涉及费用调整
 */
@Data
@Accessors(chain = true)
public class AnomalDeductCompensationDto implements Serializable {
    /**
     * 扣款 补款对象类型
     */
    private Integer userType;
    /**
     * 金额
     */
    private BigDecimal money;
}
