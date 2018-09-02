package com.jaagro.tms.api.dto.base;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ShowUserDto implements Serializable {

    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户类型
     */
    private String userType;
}
