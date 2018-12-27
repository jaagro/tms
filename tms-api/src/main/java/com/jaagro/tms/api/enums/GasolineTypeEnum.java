package com.jaagro.tms.api.enums;

/**
 * @author @Gao.
 */
public enum GasolineTypeEnum {
    /**
     * 燃油类型
     */
    NINETY(1, "0#", "0号"),
    NINETY_THREE(2, "-10#", "10号"),
    NINETY_FIVE(3, "-35#", "-35号"),
    OTHER(4, "other", "其他");
    private int code;
    private String desc;
    private String type;

    GasolineTypeEnum(int code, String desc, String type) {
        this.code = code;
        this.desc = desc;
        this.type = type;
    }

    public static String getDesc(int code) {
        for (GasolineTypeEnum type : GasolineTypeEnum.values()) {
            if (type.getCode() == code) {
                return type.getDesc();
            }
        }
        return null;
    }

    public static String getTypeByDesc(String desc) {
        for (GasolineTypeEnum type : GasolineTypeEnum.values()) {
            if (type.getDesc().equalsIgnoreCase(desc)) {
                return type.getType();
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (GasolineTypeEnum type : GasolineTypeEnum.values()) {
            if (type.getDesc().equalsIgnoreCase(desc)) {
                return type.getCode();
            }
        }
        return null;
    }

    public static GasolineTypeEnum toEnum(int code) {
        for (GasolineTypeEnum type : GasolineTypeEnum.values()) {
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
