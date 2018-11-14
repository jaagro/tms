package com.jaagro.tms.web.vo.receipt;

import com.jaagro.tms.web.vo.chat.WaybillTrackingVo;
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
     * 提货信息
     */
    private List<WaybillGoodsVo> loadGoodsList;
    /**
     * 卸货信息
     */
    private List<WaybillGoodsVo> unLoadGoodsList;
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
     *  货物类型
     */
    private Integer goodsType;
}
