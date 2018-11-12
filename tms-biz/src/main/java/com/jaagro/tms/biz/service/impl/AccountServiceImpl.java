package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.dto.account.AccountReturnDto;
import com.jaagro.tms.api.dto.account.QueryAccountDto;
import com.jaagro.tms.api.service.AccountService;
import com.jaagro.tms.biz.service.AccountClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yj
 * @date 2018/11/12
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountClientService accountClientService;
    /**
     * 查询账户信息
     *
     * @param queryAccountDto
     * @return
     */
    @Override
    public AccountReturnDto getByQueryAccountDto(QueryAccountDto queryAccountDto) {
        return accountClientService.getByQueryAccountDto(queryAccountDto);
    }
}
