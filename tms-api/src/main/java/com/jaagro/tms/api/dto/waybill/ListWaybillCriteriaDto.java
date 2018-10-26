package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ListWaybillCriteriaDto implements Serializable {

    /**
     * 起始页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 运单状态：待派车、待司机接单、已接单、司机已出发、司机到达装货地、运输中、卸货完成、已完成，取消
     */
    private String waybillStatus;

    /**
     * 运单编号
     */
    private Integer id;

    /**
     * 订单编号
     */
    private Integer orderId;

    /**
     * 派单时间
     */
    private Date createTime;
    /**
     * 登陆人所在部门和下属所有部门
     */
    private List<Integer> departIds;

    /**
     * 车牌号
     */
    private String truckNumber;

    /**
     * 车辆id数组 （用于查询条件有车牌号的情况）
     */
    private List<Integer> truckIds;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 订单id数组 （用于查询条件有客户的情况）
     */
    private List<Integer> orderIds;
}
