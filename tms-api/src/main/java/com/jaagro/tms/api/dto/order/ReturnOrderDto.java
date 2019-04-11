package com.jaagro.tms.api.dto.order;

import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author baiyiran
 */
@Data
@Accessors(chain = true)
public class ReturnOrderDto implements Serializable {

    /**
     * 订单id
     */
    private Integer id;

    /**
     * 客户id
     */
    private ShowCustomerDto customerId;
}
