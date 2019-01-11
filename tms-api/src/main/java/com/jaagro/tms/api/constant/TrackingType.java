package com.jaagro.tms.api.constant;

/**
 * 跟踪类型
 * @author yj
 * @date 2018/10/31
 */
public final class TrackingType {
    /**
     * 运输轨迹(默认)
     */
    public static final Integer TRANSPORT = 1;
    /**
     * 提货补录
     */
    public static final Integer LOAD_RECEIPT = 2;
    /**
     * 卸货补录
     */
    public static final Integer UNLOAD_RECEIPT = 3;
    /**
     * 提货单据补录
     */
    public static final Integer LOAD_BILLS_RECEIPT = 4;
    /**
     * 卸货单据补录
     */
    public static final Integer UNLOAD_BILLS_RECEIPT = 5;

    /**
     * 运单系统自动拒单
     */
    public static final Integer WAYBILL_SYSTEM_REJECT = 6;

    /**
     * 运单异常重置派单
     */
    public static final Integer ANOMALY_WAYBILL_RESET = 7;


}
