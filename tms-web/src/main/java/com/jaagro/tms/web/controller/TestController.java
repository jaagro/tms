package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.LoadExcelDto;
import com.jaagro.tms.api.dto.UserDto;
import com.jaagro.tms.api.service.UserClientService;
import com.jaagro.tms.biz.service.OldUserService;
import com.jaagro.tms.biz.service.CodeGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tony
 */
@RestController
public class TestController {

    @Autowired
    private OldUserService oldUserService;

    @Autowired
    UserClientService userClientService;

    @Autowired
    private CodeGenerationService codeGenerationService;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/test")
    public String test(){
        return "success";
    }

    @GetMapping("/test2")
    public boolean test2(){
        LoadExcelDto dto = new LoadExcelDto();
        return StringUtils.isEmpty(dto);
    }

    @GetMapping("/test3")
    public UserDto test3(){
        String token = request.getHeader("token");
        return userClientService.getUserByToken(token);
    }
//    @GetMapping("/test4")
//    public Branch test4(){
//        return userService.getCurrentBranch();
//    }

//    @GetMapping("/test5")
//    public void test5(String token){
//        System.out.println(tokenClient.getTokenInfo(token));
//    }
}
