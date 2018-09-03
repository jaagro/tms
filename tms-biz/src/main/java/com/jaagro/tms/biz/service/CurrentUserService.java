package com.jaagro.tms.biz.service;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.api.service.UserClientService;
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
}