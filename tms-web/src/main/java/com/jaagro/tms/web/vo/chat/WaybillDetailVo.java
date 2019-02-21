package com.jaagro.tms.web.vo.chat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @Author gavin
 * 微信小程序运单详情vo
 */
@Data
@Accessors(chain = true)
public class WaybillDetailVo {
    /**
     *
     */
    private Integer id;
    /**
     * 运单状态：待派车、待司机接单、已接单、司机已出发、司机到达装货地、运输中、卸货完成、已完成，取消
     */
    private String waybillStatus;
    /**
     * 货物类型
     */
    private Integer goodType;
    /**
     * 装货时间
     */
    private Date loadTime;
    /**
     * 装货地
     */
    private ShowSiteVo loadSiteVo;

    /**
     * 指派车辆的车牌号码
     */
    private String truckNumber;

    /**
     * 司机名字
     */
    private String driverName;

    /**
     * 司机手机号码
     */
    private String driverPhoneNumber;

    /**
     * 用于判断当前运单是否评价
     */
    private boolean waybillEvaluate;

    /**
     * 运单明细list
     */
    private List<WaybillItemsVo> waybillItems;

    /**
     * 运单轨迹
     */
    private List<WaybillTrackingVo> tracking;

}
