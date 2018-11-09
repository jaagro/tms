package com.jaagro.tms.api.dto.order;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class WeChatOrderSiteDto implements Serializable {

    private Integer id;

    /**
     * 装货地名称
     */
    private String siteName;

    /**
     * 装货信息 提货要求时间
     */
    private Date loadTime;

    /**
     * 卸货信息 要求送货时间
     */
    private Date requiredTime;
}
