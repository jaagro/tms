package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.customer.ShowCustomerContractDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author tony
 */
@FeignClient(value = "crm")
public interface CustomerClientService {

    /**
     * 从crm项目获取显示客户对象
     * @param id
     * @return
     */
    @GetMapping("/getShowCustomer/{id}")
    ShowCustomerDto getShowCustomerById(@PathVariable("id") Integer id);

    /**
     * 获取客户合同显示对象
     * @param id
     * @return
     */
    @GetMapping("/getShowCustomerContract/{id}")
    ShowCustomerContractDto getShowCustomerContractById(@PathVariable("id") Integer id);

    /**
     * 获取装卸货地显示对象
     * @param id
     * @return
     */
    @GetMapping("/getShowSite/{id}")
    ShowSiteDto getShowSiteById(@PathVariable("id") Integer id);
}
