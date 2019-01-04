package com.jaagro.tms.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.CustomerType;
import com.jaagro.tms.api.dto.base.GetCustomerUserDto;
import com.jaagro.tms.api.dto.base.MyInfoVo;
import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.biz.service.AuthClientService;
import com.jaagro.tms.biz.service.CustomerClientService;
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
    @Autowired
    private CustomerClientService customerClientService;

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

    /**
     * 根据当前用户获取基本信息【客户/装货地/卸货地】
     *
     * @return
     */
    public MyInfoVo getWebChatCurrentByCustomerUser() {
        //获得customer_user中的关联用户id
        GetCustomerUserDto customerUserDto = this.getCustomerUserById();
        if (customerUserDto == null) {
            return null;
        }
        MyInfoVo myInfoVo = new MyInfoVo();
        if (customerUserDto.getCustomerType().equals(CustomerType.CUSTOER)) {
            ShowCustomerDto customerDto = customerClientService.getShowCustomerById(customerUserDto.getRelevanceId());
            if (customerDto != null) {
                myInfoVo
                        .setCustomerId(customerDto.getId())
                        .setName(customerDto.getCustomerName())
                        .setPhone(customerUserDto.getPhoneNumber())
                        .setUserType(CustomerType.CUSTOER);
            }
        } else {
            ShowSiteDto showSiteDto = customerClientService.getShowSiteById(customerUserDto.getRelevanceId());
            if (showSiteDto != null) {
                myInfoVo
                        .setSiteId(showSiteDto.getId())
                        .setCustomerId(showSiteDto.getCustomerId())
                        .setName(showSiteDto.getSiteName())
                        .setPhone(customerUserDto.getPhoneNumber());
                //1-装货点，2-卸货点
                if (showSiteDto.getSiteType() != null && showSiteDto.getSiteType().equals(1)) {
                    myInfoVo.setUserType(CustomerType.LOAD_SITE);
                } else {
                    myInfoVo.setUserType(CustomerType.UNLOAD_SITE);
                }
            }
        }
        return myInfoVo;
    }
}