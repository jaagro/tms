package com.jaagro.tms.api.dto.account;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yj
 * @date 2018/10/25
 */
@Data
@Accessors(chain = true)
public class QueryAccountDto implements Serializable{
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户类型1-客户 2-司机
     */
    private Integer userType;
    /**
     * 账户类型1-现金账户 2-保证金账户
     */
    private Integer accountType;
}
