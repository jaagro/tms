package com.jaagro.tms.api.dto.settlemanage;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 结算管理运单费用查询条件
 * @author: @Gao.
 * @create: 2019-04-11 17:02
 **/
@Data
@Accessors(chain = true)
public class WaybillFeeCriteria implements Serializable {
    /**
     * 起始页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 运单号
     */
    private Integer waybillId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 车牌号
     */
    private String truckNumber;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 货物类型
     */
    private Integer goodsType;
}
