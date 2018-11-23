package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.constant.GoodsType;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.service.CustomerReportService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.entity.CustomerReport;
import com.jaagro.tms.biz.service.CustomerClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author baiyiran
 * @Date 2018/11/22
 */
@Slf4j
@Service
public class CustomerReportServiceImpl implements CustomerReportService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private WaybillService waybillService;
    @Autowired
    private CustomerClientService customerClientService;

    /**
     * 计算客户报表
     *
     * @return
     */
    @Override
    public Integer customerReportAccount() {
        log.info("\n计算客户报表开始");
        //全部正常合作的客户
        List<ShowCustomerDto> customerDtoList = customerClientService.listNormalCustomer();
        //今日客户报表
        List<CustomerReport> reportList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(customerDtoList)) {
            for (ShowCustomerDto customerDto : customerDtoList) {
                /**
                 * 客户-毛鸡（goodsType:1）
                 */
                CustomerReport report1 = new CustomerReport();
                report1.setGoodsType(GoodsType.CHICKEN);
                Boolean result1 = paddingReport(report1, customerDto);
                if (result1) {
                    reportList.add(report1);
                }
                /**
                 * 客户-饲料（goodsType:2）
                 */
                CustomerReport report2 = new CustomerReport();
                report2.setGoodsType(GoodsType.FODDER);
                Boolean result2 = paddingReport(report2, customerDto);
                if (result2) {
                    reportList.add(report2);
                }
                /**
                 * 客户-猪（goodsType:3、4、5、6）
                 */
                CustomerReport report3 = new CustomerReport();
                report3.setGoodsType(GoodsType.SOW);
                Boolean result3 = paddingReport(report3, customerDto);
                if (result3) {
                    reportList.add(report3);
                }
            }
        }
        if (CollectionUtils.isEmpty(reportList)) {
            return 0;
        } else {
            return reportList.size();
        }
    }

    /**
     * 根据不同货物类型统计
     */
    private Boolean paddingReport(CustomerReport report, ShowCustomerDto customerDto) {
        if (report != null && customerDto != null && report.getGoodsType() != null) {

            //客户id & 客户名称
            BeanUtils.copyProperties(customerDto, report);
            //统计时间
            report.setReportTime(new Date());

            //订单总数


            //运单总数

            //异常单数

            //数量(单位)

            //吨位(单位)

            //收入-运费

            //收入-异常费用

            //成本-运费

            //成本-异常费用

            //毛利

            //毛利率
        }
        return null;
    }
}
