package com.jaagro.tms.biz.service;

import com.jaagro.utils.BaseResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author gavin
 */
@FeignClient("component")
public interface SmsClientService {

    /**
     * @Author gavin
     * @param phoneNumber 接收短信的手机号码
     * @param templateCode 短信模版
     * @param templateMap 模版参数
     * @return
     */
    @PostMapping("/sendSMS")
    BaseResponse sendSMS(@RequestParam("phoneNumber") String phoneNumber,@RequestParam("templateCode") String templateCode,@RequestParam("templateMap") Map<String, Object> templateMap);

}
