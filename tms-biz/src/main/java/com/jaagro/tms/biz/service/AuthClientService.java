package com.jaagro.tms.biz.service;

import com.jaagro.constant.UserInfo;
import com.jaagro.utils.BaseResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author tony
 */
@FeignClient(value = "auth")
public interface AuthClientService {
    /**
     * 获取token相关的用户信息
     * @param token
     * @return
     */
    @PostMapping("/getUserByToken")
    UserInfo getUserByToken(@RequestParam("token") String token);

    /**
     * 通过id获取user
     * @param id
     * @param userType
     * @return
     */
    @PostMapping("getUserById")
    UserInfo getUserInfoById(@RequestParam("id") Integer id, @RequestParam("userType") String userType);


}
