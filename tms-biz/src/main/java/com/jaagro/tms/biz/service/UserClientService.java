package com.jaagro.tms.biz.service;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.base.DepartmentReturnDto;
import com.jaagro.utils.BaseResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author tony
 */
@FeignClient("user")
public interface UserClientService {

    /**
     * 获取当前token可查询的数据范围 -- 依据部门id
     *
     * @return
     */
    @PostMapping("/getDownDepartment")
    List<Integer> getDownDepartment();

    /**
     * 获取指定部门id及下属部门id数组
     *
     * @return
     */
    @PostMapping("/getDownDepartmentByDeptId/{deptId}")
    List<Integer> getDownDepartmentByDeptId(@PathVariable("deptId") Integer deptId);

    /**
     * 根据部门id查询部门名称
     *
     * @param id
     * @return
     */
    @GetMapping("/getDeptNameById/{id}")
    String getDeptNameById(@PathVariable("id") Integer id);

    /**
     * 根据id列表查用户列表
     *
     * @param userIdList
     * @param userType
     * @return
     */
    @GetMapping("/listUserInfo")
    List<UserInfo> listUserInfo(@RequestParam("userIdList") List<Integer> userIdList, @RequestParam("userType") String userType);

    /**
     * 根据userId获取对应的用户数据
     *
     * @param userId
     * @return
     */
    @GetMapping("/getGlobalUser/{userId}")
    BaseResponse<UserInfo> getGlobalUser(@PathVariable("userId") int userId);

    @PostMapping("/getAllDepartments")
    List<DepartmentReturnDto> getAllDepartments();

}
