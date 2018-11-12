package com.jaagro.tms.api.constant;

/**
 * 异常状态
 *
 * @author tony
 */
public final class AnomalyStatus {

    public final static String TO_DO = "待处理";

    public final static String DONE = "已处理";

    public final static String TO_AUDIT = "待审核";

    public final static String FINISH = "已结束";

    public final static String INVALID = "已作废";

    public final static String REFUSE = "审核不通过";

    //=======审核状态========

    public final static String AUDIT_APPROVAL = "已通过";

    public final static String AUDIT_REFUSEL = "已拒绝";

    public final static String OK = "通过";

    public final static String NO = "拒绝";
}
