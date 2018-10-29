package com.jaagro.tms.biz.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
     * 根据部门id查询部门名称
     *
     * @param id
     * @return
     */
    @GetMapping("/getDeptNameById/{id}")
    String getDeptNameById(@PathVariable("id") Integer id);
}
