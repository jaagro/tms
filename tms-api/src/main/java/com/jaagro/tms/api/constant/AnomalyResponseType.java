package com.jaagro.tms.api.constant;

/**
 * @author @Gao.
 */
public final class AnomalyResponseType {
    /**
     * 表示当前运单已完成 不能改派
     */
    public final static String WAYBILL_ACCOMPLISH = "WAYBILL_ACCOMPLISH";
    /**
     * 当前改派类型已经存在不能再改派
     */
    public final static String CANCEL_WAYBILL_EXIST = "CANCEL_WAYBILL_EXIST";
}
