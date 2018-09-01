package com.jaagro.tms.api.dto.customer;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ShowSiteDto implements Serializable {

    private Integer id;

    /**
     * 地址类型：1-装货点，2-卸货点
     */
    private Integer siteType;

    /**
     * 装货地名称
     */
    private String siteName;
}
