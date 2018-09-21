package com.jaagro.tms.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.biz.service.UserClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tony
 */
@Service
public class CurrentUserService {
    @Autowired
    private UserClientService tokenClient;
    @Autowired
    private HttpServletRequest request;

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
        if(null == result){
            throw new NullPointerException("当前用户不存在");
        }
        return result;
    }
}