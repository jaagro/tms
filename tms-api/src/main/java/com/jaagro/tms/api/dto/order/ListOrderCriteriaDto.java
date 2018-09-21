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
     * 客户id
     */
    private Integer customerId;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 开单时间
     */
    private Date createTime;

}
