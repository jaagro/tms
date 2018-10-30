package com.jaagro.tms.api.dto.customer;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ShowSiteDto implements Serializable {

    private Integer id;

    /**
     * 归属网点
     */
    private Integer deptId;

    /**
     * 归属网点名称
     */
    private String deptName;

    /**
     * 地址类型：1-装货点，2-卸货点
     */
    private Integer siteType;

    /**
     * 装货地名称
     */
    private String siteName;

    /**
     * 联系人姓名
     */
    private String contact;

    /**
     * 养殖场类型（装货地属性）：1-平养场 2-网养场 3-笼养场
     */
    private Integer farmsType;

    /**
     * 作业时间（装货地属性）
     */
    private Integer operationTime;

    /**
     * 屠宰链数(卸货地属性)
     */
    private Integer killChain;

    /**
     * 开始屠宰时间（卸货地属性）
     */
    private Date killTime;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private String county;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 装货信息 提货要求时间
     */
    private Date loadTime;

    /**
     * 卸货信息 要求送货时间
     */
    private Date requiredTime;
    /**
     * 货物类型:毛鸡、饲料、母猪、公猪、仔猪、生猪
     */
    private int productType;
}
