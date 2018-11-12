package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.account.AccountReturnDto;
import com.jaagro.tms.api.dto.account.QueryAccountDto;

/**
 * 账户查询,操作
 * @author yj
 * @date 2018/10/26
 */
public interface AccountService {
    /**
     * 查询账户信息
     * @param queryAccountDto
     * @return
     */
    AccountReturnDto getByQueryAccountDto(QueryAccountDto queryAccountDto);
}
