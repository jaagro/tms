package com.jaagro.tms.api.constant;

/**
 * 回单补录状态
 * @author yj
 * @since 2018/12/11
 */
public class ReceiptStatus {
    /**
     * 未回单补录
     */
    public static final Integer UN_RECEIPT = 0;
    /**
     * 已补录实提
     */
    public static final Integer LOAD_RECEIPT = 1;
    /**
     * 已补录实卸
     */
    public static final Integer UNLOAD_RECEIPT = 2;

}
