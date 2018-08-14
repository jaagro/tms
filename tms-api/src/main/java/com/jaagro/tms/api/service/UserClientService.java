package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author tony
 */
@FeignClient(value = "auth")
public interface UserClientService {
    /**
     * 获取token相关的用户信息
     * 提供给其他微服务使用
     * @param token
     * @return
     */
    @PostMapping("/getUserByToken")
    UserDto getUserByToken(@RequestParam("token") String token);
}
