package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.UserDto;
import com.jaagro.tms.api.service.UserClientService;
import com.jaagro.tms.biz.entity.Branch;
import com.jaagro.tms.biz.entity.User;
import com.jaagro.tms.biz.mapper.BranchMapper;
import com.jaagro.tms.biz.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于通过老系统token获取user信息
 * @author tony
 */
@Service
public class OldUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BranchMapper branchMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserClientService userClientService;

    public User getCurrentUser(){
        UserDto userDto = getCurrentTokenInfo();
        String username = userDto.getUsername();
        User user = userMapper.selectByUsername(username);
        return user;
    }

    public Branch getCurrentBranch(){
        Long userId = getCurrentUser().getUserId();
        Branch userBranch = branchMapper.getBranchByUser(userId);
        return userBranch;
    }

    public UserDto getCurrentTokenInfo(){
        String token = request.getHeader("token");
        return userClientService.getUserByToken(token);
    }
}
