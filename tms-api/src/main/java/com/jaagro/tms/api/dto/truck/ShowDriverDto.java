package com.jaagro.tms.api.dto.truck;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ShowDriverDto implements Serializable {

    private Integer id;

    /**
     * 司机名
     */
    private String driverName;

    /**
     * 是不是主驾
     */
    private Boolean maindriver;

    /**
     * 手机号码
     */
    private String phoneNumber;
}
