package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gavin
 *
 * @Datetime 20190107
 */
@Data
@Accessors(chain = true)
public class ImportWaybillDto implements Serializable {


    private static final long serialVersionUID = -8522034505600662869L;
    /**
     * 订单编号
     */
    private Integer orderId;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 装货地id
     */
    private Integer loadSiteId;

    /**
     * 装货地名称
     */
    private String loadSiteName;

    /**
     * 装货地所属网点id
     */
    private Integer loadSiteDeptId;

    /**
     * 车辆id
     */
    private Integer truckId;

    /**
     * 车牌号
     */
    private String truckNumber;

    /**
     * 车队合同id
     */
    private Integer truckTeamContractId;

    /**
     * 车型id
     */
    private Integer truckTypeId;

    /**
     * 车型名称
     */
    private String truckTypeName;

    /**
     * 货物数量(单位-筐)
     */
    private Integer goodsQuantity;

    /**
     * 要求装货时间
     */
    private Date loadTime;

    /**
     * 要求送达时间
     */
    private Date requiredTime;
}
