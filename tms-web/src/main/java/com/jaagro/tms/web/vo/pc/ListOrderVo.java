package com.jaagro.tms.web.vo.pc;

import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerContractDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author baiyiran
 */
@Data
@Accessors(chain = true)
public class ListOrderVo implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 客户合同id
     */
    private ShowCustomerContractDto customerContract;

    /**
     * 货物类型
     */
    private Integer goodsType;

    /**
     * 订单状态:
     */
    private String orderStatus;

    /**
     * 客户id
     */
    private ShowCustomerDto customerId;

    /**
     * 装货地id
     */
    private ShowSiteDto loadSite;

    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 要求装货时间
     */
    private Date loadTime;

    /**
     * 是否需要纸质回单
     */
    private Boolean paperReceipt;

    /**
     * 备注
     */
    private String notes;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private ShowUserDto createdUserId;

    /**
     * 计划派单
     */
    private Integer waybillCount;

    /**
     * 已派单
     */
    private Integer waybillAlready;

    /**
     * 已拒单
     */
    private Integer waybillReject;

    /**
     * 待派单
     */
    private Integer waybillWait;

    /**
     * 订单需求列表
     */
    private List<ListOrderItemsVo> orderItemsVoList;

}
