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
     * 运单明细list
     */
    private List<WaybillItemsVo> waybillItems;

    /**
     * 运单轨迹
     */
    private List<WaybillTrackingVo> tracking;

}
