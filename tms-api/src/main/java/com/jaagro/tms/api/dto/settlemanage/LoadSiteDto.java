package com.jaagro.tms.api.dto.settlemanage;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 装货信息
 * @author: @Gao.
 * @create: 2019-04-11 16:14
 **/
@Data
@Accessors(chain = true)
public class LoadSiteDto implements Serializable {
    /**
     * 装货地名称
     */
    private String loadSiteName;

    /**
     * 装货时间
     */
    private Date loadSiteTime;

}
