package com.jaagro.tms.web.vo.chat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author baiyiran
 * @Date 2018/10/22
 */
@Data
@Accessors(chain = true)
public class SiteVo implements Serializable {

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
     * 装货地名称
     */
    private String siteName;

    /**
     * 联系人姓名
     */
    private String contact;

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
     * 装货信息 提货要求时间
     */
    private Date loadTime;

    /**
     * 卸货信息 要求送货时间
     */
    private Date requiredTime;

}
