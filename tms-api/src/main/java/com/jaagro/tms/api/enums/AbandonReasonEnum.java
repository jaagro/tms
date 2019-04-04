package com.jaagro.tms.api.enums;

/**
 * 运单作废原因
 */
public enum AbandonReasonEnum {
    /**
     * 1: "原运单计划有变; 2: "运单信息有错误"; 3:  "其它原因"
     */
    PLAN_CHANGE(1, "PLAN_CHANGE", "原运单计划有变"),
    INFO_ERROR(2, "INFO_ERROR", "运单信息有错误"),
    OTHER_REASON(3, "OTHER_REASON", "其它原因");
    private int code;
    private String type;
    private String desc;

    AbandonReasonEnum(int code, String type, String desc) {
        this.code = code;
        this.type = type;
        this.desc = desc;
    }

    public static String getDescByCode(int code) {
        for (AbandonReasonEnum type : AbandonReasonEnum.values()) {
            if (type.getCode() == code) {
                return type.getDesc();
            }
        }
        return null;
    }

    public static String getTypeByCode(int code) {
        for (AbandonReasonEnum type : AbandonReasonEnum.values()) {
            if (type.getCode() == code) {
                return type.getType();
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (AbandonReasonEnum type : AbandonReasonEnum.values()) {
            if (type.getDesc().equalsIgnoreCase(desc)) {
                return type.getCode();
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static void main(String[] args) {
        System.out.println(getDescByCode(3));
    }
}
