package com.jaagro.tms.web.vo.receipt;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 运单回单记录
 * @author yj
 * @date 2018/10/31
 */
@Data
@Accessors(chain = true)
public class WayBillReceiptsVo implements Serializable{
    /**
     * 运单货物信息
     */
    private List<WaybillGoodsVo> waybillGoodsList;
    /**
     * 提货轨迹图片
     */
    private List<WaybillTrackingImagesVo> loadImagesList;
    /**
     * 卸货轨迹图片
     */
    private List<WaybillTrackingImagesVo> unLoadImagesList;
    /**
     * 提货补录记录
     */
    private WayBillTrackingVo loadTracking;
    /**
     * 卸货补录记录
     */
    private WayBillTrackingVo unLoadTracking;
    /**
     * 客户id
     */
    private Integer customerId;
    /**
     * 货物类型(1-毛鸡,2-饲料,3-母猪,4-公猪,5-仔猪,6-生猪)
     */
    private Integer goodsType;
    /**
     * 货物单位(1-筐,2-头,3-吨)
     */
    private Integer goodsUnit;
}
