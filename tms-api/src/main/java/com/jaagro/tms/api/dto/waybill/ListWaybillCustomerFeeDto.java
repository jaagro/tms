package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author baiyiran
 * @date 2019/03/04
 */
@Data
@Accessors(chain = true)
public class ListWaybillCustomerFeeDto implements Serializable {

    /**
     * 起始页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 项目部
     */
    private List<Integer> departId;

    /**
     * 项目部
     */
    private List<Integer> departIds;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 货物类型
     */
    private Integer goodsType;
}
