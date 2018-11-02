package com.jaagro.tms.web.vo.chat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author baiyiran
 * @Date 2018/10/22
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
    private ShowCustomerContractVo customerContract;

    /**
     * 客户联系人
     */
    private CustomerContactsVo contactsDto;

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
    private CustomerVo customer;

    /**
     * 装货地id
     */
    private SiteVo loadSiteId;

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
    private UserVo createdUser;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人id
     */
    private UserVo modifyUser;

    /**
     * 订单列表
     */
    private List<ListOrderItemsVo> itemsVoList;

}
