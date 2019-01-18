package com.jaagro.tms.api.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 毛鸡导入记录
 * @author yj
 * @date 2019-01-07 16:32
 */
@Data
@Accessors(chain = true)
public class ChickenImportRecord implements Serializable{
    private static final long serialVersionUID = -7443340151581736114L;
    /**
     * 毛鸡导入记录表id
     */
    private Integer id;

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

    /**
     * 是否有效：1-有效 0-无效
     */
    private Boolean enable;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 更新人
     */
    private Integer modifyUserId;

}