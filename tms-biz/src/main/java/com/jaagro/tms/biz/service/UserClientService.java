package com.jaagro.tms.biz.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

/**
 * @author tony
 */
@FeignClient("user")
public interface UserClientService {

    /**
     * 获取当前token可查询的数据范围 -- 依据部门id
     * @return
     */
    @PostMapping("/getDownDepartment")
    Set<Integer> getDownDepartment();
}
