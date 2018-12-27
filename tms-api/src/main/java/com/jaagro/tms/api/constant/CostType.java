package com.jaagro.tms.api.constant;

/**
 * @author tony
 */
public final class CostType {

    /**
     * 运费
     */
    public final static Integer FREIGHT = 1;

    /**
     * 附加费
     */
    public final static Integer ADDITIONAL = 2;

    /**
     * 赔款
     */
    public final static Integer COMPENSATE = 3;

    /**
     * 扣款
     */
    public final static Integer DEDUCTION = 4;

    /**
     * 卸货费
     */
    public final static Integer UNLOAD_FEE = 5;

    /**
     *货损费
     */
    public final static Integer DAMAGE_FEE = 6;

    /**
     * 资金方向(1-增加,2-减少)
     *
     */
    public final static Integer DIRECTION_INCREASE = 1;
    /**
     *资金方向(1-增加,2-减少)
     */
    public final static Integer DIRECTION_DECREASE = 2;
}
