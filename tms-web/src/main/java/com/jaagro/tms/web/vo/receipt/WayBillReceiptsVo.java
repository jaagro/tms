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
     * 运单货物信息
     */
    private List<WaybillGoodsVo> waybillGoodsVoList;
    /**
     * 运单轨迹信息(补录)
     */
    private List<WayBillTrackingVo> wayBillTrackingVoList;
    /**
     * 运单轨迹图片
     */
    private List<WaybillTrackingImagesVo> waybillTrackingImagesVoList;
    /**
     * 客户id
     */
    private Integer customerId;
    /**
     *  货物类型
     */
    private Integer goodsType;
}
