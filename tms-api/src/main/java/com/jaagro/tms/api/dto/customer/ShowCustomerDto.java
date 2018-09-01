package com.jaagro.tms.api.dto.customer;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ShowCustomerDto implements Serializable {

    private Integer id;

    /**
     * 客户名称(个体客户时，就是自然人姓名)
     */
    private String customerName;
}
