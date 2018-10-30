package com.jaagro.tms.web.vo.chat;

import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.dto.waybill.GetTrackingDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillItemDto;
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
    private ShowSiteVo showSiteVo;

    /**
     * 运单指派的车辆详情
     */
    private ShowTruckDto assginedTruckDto;

    /**
     * 司机详情
     */
    private ShowDriverDto driverDto;


    /**
     * 运单明细list
     */
    private List<GetWaybillItemDto> waybillItems;

    /**
     * 运单轨迹
     */
    private List<GetTrackingDto> tracking;

    /**
     * 货物类型
     */
    private Integer goodType;

}
