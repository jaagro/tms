package com.jaagro.tms.api.enums;

/**
 * @author @Gao.
 */
public enum GasolineCompanyNameEnum {
    /**
     * 加油公司名称
     */
    PETROCHINA(1, "petrochina", "中石油"),
    SINOPEC(2, "sinopec", "中石化"),
    CNOOC(3, "cnooc", "中海油"),
    OTHER(4, "other", "其他");
    private int code;
    private String desc;
    private String type;

    GasolineCompanyNameEnum(int code, String desc, String type) {
        this.code = code;
        this.desc = desc;
        this.type = type;
    }

    public static String getDesc(int code) {
        for (GasolineCompanyNameEnum type : GasolineCompanyNameEnum.values()) {
            if (type.getCode() == code) {
                return type.getDesc();
            }
        }
        return null;
    }

    public static String getTypeByDesc(String desc) {
        for (GasolineCompanyNameEnum type : GasolineCompanyNameEnum.values()) {
            if (type.getDesc().equalsIgnoreCase(desc)) {
                return type.getType();
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        for (GasolineCompanyNameEnum type : GasolineCompanyNameEnum.values()) {
            if (type.getDesc().equalsIgnoreCase(desc)) {
                return type.getCode();
            }
        }
        return null;
    }

    public static GasolineCompanyNameEnum toEnum(int code) {
        for (GasolineCompanyNameEnum type : GasolineCompanyNameEnum.values()) {
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
