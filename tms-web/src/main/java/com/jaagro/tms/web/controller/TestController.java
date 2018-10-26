package com.jaagro.tms.web.controller;

import com.jaagro.annotation.RequiredPermission;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerContractDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.dto.waybill.CreateWaybillPlanDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillDto;
import com.jaagro.tms.api.dto.waybill.ListWaybillPlanDto;
import com.jaagro.tms.api.service.WaybillPlanService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.SmsClientService;
import com.jaagro.tms.biz.service.TruckClientService;
import com.jaagro.tms.biz.service.TruckTypeClientService;
import com.jaagro.tms.biz.service.impl.CurrentUserService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.BeanDifferentUtils;
import com.jaagro.utils.DifferentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
@RestController
public class TestController {

}