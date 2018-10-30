package com.jaagro.tms.web.vo.chat;

import com.jaagro.tms.api.dto.waybill.GetTrackingDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * Author gavin
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
     * 司机名
     */
    private String name;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 运单明细list
     */
    private List<WaybillItemVo> waybillItems;

    /**
     * 运单轨迹
     */
    private List<GetTrackingDto> tracking;

    /**
     * 货物类型
     */
    private Integer goodType;

}
