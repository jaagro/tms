package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.service.OrderModifyLogService;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.biz.mapper.OrderModifyLogMapper;
import com.jaagro.tms.biz.mapper.OrdersMapper;
import com.jaagro.tms.biz.mapper.OrdersMapperExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author baiyiran
 */
@Service
public class OrderModifyLogServiceImpl implements OrderModifyLogService {

    @Autowired
    private OrderModifyLogMapper modifyLogMapper;
    @Autowired
    private CurrentUserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrdersMapperExt ordersMapper;

}
