package com.jaagro.tms.api.constant;

import java.math.BigDecimal;

/**
 * @author @Gao.
 */
public final class DataConstant {
    /**
     * 主要用于磅差超过千分之二，进行预警提醒
     */
    public static final BigDecimal DIFFWEIGHT =new BigDecimal(0.002);

}
