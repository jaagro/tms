package com.jaagro.tms.web.vo.chat;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author gavin
 * 微信小程序运单装货信息
 */
@Data
@Accessors(chain = true)
public class ShowSiteVo {

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


}
