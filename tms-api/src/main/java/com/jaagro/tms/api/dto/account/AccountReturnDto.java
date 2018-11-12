package com.jaagro.tms.api.dto.account;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户信息返回
 * @author yj
 * @date 2018/10/25
 */
@Data
@Accessors(chain = true)
public class AccountReturnDto implements Serializable{
    /**
     * 账户id
     */
    private Integer id;

    /**
     * 乐观锁版本号
     */
    private Integer version;

    /**
     * 账户类型1-现金账户 2-保证金账户
     */
    private Integer accountType;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户类型1-客户 2-司机
     */
    private Integer userType;

    /**
     * 支出总额
     */
    private BigDecimal credit;

    /**
     * 收入总额
     */
    private BigDecimal debit;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 账户状态1-正常 2-止收 3-止支 4-冻结
     */
    private Integer accountStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 修改人
     */
    private Integer modifyUserId;
}
