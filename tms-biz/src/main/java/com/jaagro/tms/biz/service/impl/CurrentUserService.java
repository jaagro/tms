package com.jaagro.tms.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.base.GetCustomerUserDto;
import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.biz.service.AuthClientService;
import com.jaagro.tms.biz.service.CustomerUserClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tony
 */
@Service
public class CurrentUserService {
    @Autowired
    private AuthClientService tokenClient;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CustomerUserClientService customerUserClientService;

    public UserInfo getCurrentUser() {
        String token = request.getHeader("token");
        return tokenClient.getUserByToken(token);
    }

    public ShowUserDto getShowUser() {
        UserInfo userInfo = this.getCurrentUser();
        ShowUserDto showUser = new ShowUserDto();
        BeanUtils.copyProperties(userInfo, showUser);
        return showUser;
    }

    public UserInfo getUserInfoById(Integer id, String userType) {
        UserInfo result = tokenClient.getUserInfoById(id, userType);
        if (null == result) {
            throw new NullPointerException("当前用户不存在");
        }
        return result;
    }

    public GetCustomerUserDto getCustomerUserById() {
        return customerUserClientService.getCustomerUserById(getCurrentUser().getId());
    }
}