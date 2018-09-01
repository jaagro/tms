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
}
