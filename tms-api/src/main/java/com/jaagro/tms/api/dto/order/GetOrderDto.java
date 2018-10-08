package com.jaagro.tms.api.dto.order;

import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.api.dto.customer.CustomerContactsReturnDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerContractDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.driverapp.ShowSiteAppDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class GetOrderDto implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 客户合同id
     */
    private ShowCustomerContractDto customerContract;

    /**
     * 客户联系人
     */
    private CustomerContactsReturnDto contactsDto;

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
    private ShowCustomerDto customer;

    /**
     * 装货地id
     */
    private ShowSiteDto loadSiteId;

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
    private ShowUserDto createdUser;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人id
     */
    private ShowUserDto modifyUser;

    /**
     * 订单列表
     */
    private List<GetOrderItemsDto> orderItems;
}
