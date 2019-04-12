package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.settlemanage.WaybillFeeCriteria;


public interface SettleManageService {

    /**
     * 运单结算费用报表
     *
     * @param criteria
     * @return
     */
    PageInfo listWaybillFee(WaybillFeeCriteria criteria);
}
