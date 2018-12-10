package com.jaagro.tms.api.enums;

public enum PaymentMethodEnum {
    /**
     * 支付类型
     */
    ALIPAY(1, "alipay", "支付宝"),
    WECHAT(2, "wechat", "微信"),
    CASH(3, "cash", "现金"),
    OTHER(4, "other", "其他");
    private int code;
    private String desc;
    private String type;

    PaymentMethodEnum(int code, String desc, String type) {
        this.code = code;
        this.desc = desc;
        this.type = type;
    }

    public static String getDesc(int code) {
        for (PaymentMethodEnum type : PaymentMethodEnum.values()) {
            if (type.getCode() == code) {
                return type.getDesc();
            }
        }
        return null;
    }

    public static String getTypeByDesc(String desc) {
        for (PaymentMethodEnum type : PaymentMethodEnum.values()) {
            if (type.getDesc().equalsIgnoreCase(desc)) {
                return type.getType();
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (PaymentMethodEnum type : PaymentMethodEnum.values()) {
            if (type.getDesc().equalsIgnoreCase(desc)) {
                return type.getCode();
            }
        }
        return null;
    }

    public static PaymentMethodEnum toEnum(int code) {
        for (PaymentMethodEnum type : PaymentMethodEnum.values()) {
            if (type.getCode() == code) {
                return type;
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
}
