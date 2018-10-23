package com.jaagro.tms.web.vo.order;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author baiyiran
 * @Date 2018/10/22
 */
@Data
@Accessors(chain = true)
public class CustomerVo implements Serializable {

    private Integer id;

    /**
     * 客户名称(个体客户时，就是自然人姓名)
     */
    private String customerName;

}
