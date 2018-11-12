package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.account.AccountReturnDto;
import com.jaagro.tms.api.dto.account.QueryAccountDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 账户远程操作
 * @author yj
 * @date 2018/10/25
 */
@FeignClient(value = "account")
public interface AccountClientService {
    /**
     * 查询账户
     *@param queryAccountDto
     * @return
     */
    @PostMapping("/getByQueryAccountDto")
    public AccountReturnDto getByQueryAccountDto(@RequestBody QueryAccountDto queryAccountDto);
}