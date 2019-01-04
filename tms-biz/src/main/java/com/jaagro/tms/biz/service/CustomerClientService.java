package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.customer.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
@FeignClient(value = "${feignClient.application.crm}")
public interface CustomerClientService {

    /**
     * 从crm项目获取显示客户对象
     *
     * @param id
     * @return
     */
    @GetMapping("/getShowCustomer/{id}")
    ShowCustomerDto getShowCustomerById(@PathVariable("id") Integer id);

    /**
     * 获取客户合同显示对象
     *
     * @param id
     * @return
     */
    @GetMapping("/getShowCustomerContract/{id}")
    ShowCustomerContractDto getShowCustomerContractById(@PathVariable("id") Integer id);

    /**
     * 获取装卸货地显示对象
     *
     * @param id
     * @return
     */
    @GetMapping("/getShowSite/{id}")
    ShowSiteDto getShowSiteById(@PathVariable("id") Integer id);

    /**
     * 从crm项目获取显示客户联系人
     *
     * @param customerId
     * @return
     */
    @GetMapping("/getCustomerContactByCustomerId/{customerId}")
    CustomerContactsReturnDto getCustomerContactByCustomerId(@PathVariable("customerId") Integer customerId);

    /**
     * 根据车牌号模糊查询车辆id列表
     *
     * @param truckNumber
     * @return
     */
    @PostMapping("/getTruckIdsByTruckNum/{truckNumber}")
    List<Integer> getTruckIdsByTruckNum(@PathVariable("truckNumber") String truckNumber);

    /**
     * 查询正常合作的全部客户
     *
     * @return
     */
    @GetMapping("/listNormalCustomer")
    List<ShowCustomerDto> listNormalCustomer();

    /**
     * 根据地址id数组获得地址名称"
     *
     * @param siteIds
     * @return
     */
    @PostMapping("/listSiteNameByIds")
    List<String> listSiteNameByIds(@RequestBody List<Integer> siteIds);

    /**
     * 与客户结算的计算
     * @param dtoList
     * @return 结算金额
     */
    @PostMapping("/calculatePaymentFromCustomer")
    List<Map<Integer, BigDecimal>> calculatePaymentFromCustomer(@RequestBody  List<CalculatePaymentDto> dtoList);

    /**
     * 与司机结算的计价
     * @param dtoList
     * @return
     */
    @PostMapping("/calculatePaymentFromDriver")
    List<Map<Integer, BigDecimal>> calculatePaymentFromDriver(@RequestBody List<CalculatePaymentDto> dtoList);

}
