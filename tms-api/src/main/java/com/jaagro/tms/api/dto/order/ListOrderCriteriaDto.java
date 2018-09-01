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
public class ListOrderCriteriaDto implements Serializable {

    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 每页的数量
     */
    private int pageSize;

    /**
     * 客户名称 -- 模糊
     */
    private String customerName;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 货物类型
     */
    private Integer productType;

    /**
     * 开单人
     */
    private Integer createUserId;

    /**
     * 起始开单时间
     */
    private Date startCreateTime;

    /**
     * 截止开单时间
     */
    private Date endCreateTime;

    /**
     * 起始提货时间
     */
    private Date startLoadTime;

    /**
     * 截止提货时间
     */
    private Date endLoadTime;
}
