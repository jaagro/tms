package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.service.JinDunGpsService;
import com.jaagro.tms.biz.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 金盾服务类
 * @author: @Gao.
 * @create: 2019-04-23 14:26
 **/
@Service
@Slf4j
public class JinDunGpsServiceImpl implements JinDunGpsService {

    private static final String ACCESS_PASSWORD = "123456";
    private static final String ACCESS_NAME = "sqjd1";
    /***
     * 金盾系统登录
     * @return
     */
    @Override
    public String jinDunLogin() {
        String url = "http://120.78.214.191/StandardApiAction_login.action";
        Map<String, String> param = new HashMap<>(16);
        param.put("account", ACCESS_NAME);
        param.put("password", ACCESS_PASSWORD);
        String result = HttpClientUtil.doPost(url, param);
        log.debug("金盾接口返回：" + result);
        return result;
    }
}
