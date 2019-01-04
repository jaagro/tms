package com.jaagro.tms.api.dto.base;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author baiyiran
 * @Date 2018/11/7
 */
@Data
@Accessors(chain = true)
public class MyInfoVo implements Serializable {

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 地址id
     */
    private Integer siteId;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 名称
     */
    private String Name;

    /**
     * 联系电话
     */
    private String phone;
}
