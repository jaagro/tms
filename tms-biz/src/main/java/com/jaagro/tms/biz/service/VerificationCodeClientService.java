package com.jaagro.tms.biz.service;

import com.jaagro.utils.BaseResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author tony
 */
@FeignClient("component")
public interface VerificationCodeClientService {

    /**
     * 发送短信
     *
     * @param phoneNumber 手机号码
     * @return
     */
    @PostMapping("/sendMessage")
    BaseResponse sendMessage(@RequestParam String phoneNumber);
}
