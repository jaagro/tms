package com.jaagro.tms.api.dto.settlemanage;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 卸货时间
 * @author: @Gao.
 * @create: 2019-04-11 16:16
 **/
@Data
@Accessors(chain = true)
public class UnLoadSiteDto implements Serializable {
    /**
     * 卸货地名称
     */
    private String unLoadSiteName;

    /**
     * 卸货时间
     */
    private Date unLoadSiteTime;

}
