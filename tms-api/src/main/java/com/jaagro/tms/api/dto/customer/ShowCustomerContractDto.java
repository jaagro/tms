package com.jaagro.tms.api.dto.customer;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ShowCustomerContractDto implements Serializable {

    private Integer id;

    /**
     * 合同编号
     */
    private String contractNumber;

    /**
     * 合同类型
     */
    private Integer type;

    /**
     * 结算类型(1-按提货重量计价,2-按卸货重量计价)
     */
    private Integer settleType;
}
