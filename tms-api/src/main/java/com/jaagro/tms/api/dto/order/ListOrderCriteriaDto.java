package com.jaagro.tms.api.dto.order;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
     * 货物类型
     */
    private Integer goodsType;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 开单开始时间
     */
    private Date createStartTime;

    /**
     * 开单截止时间
     */
    private Date createEndTime;

    /**
     * 要求装货开始时间
     */
    private Date loadStartTime;

    /**
     * 要求装货截止时间
     */
    private Date loadEndTime;

    /**
     * 登陆人所在部门和下属所有部门
     */
    private List<Integer> departIds;

}
