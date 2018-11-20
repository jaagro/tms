package com.jaagro.tms.api.dto.waybill;

import com.jaagro.constant.UserInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class GetTrackingDto implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer waybillId;

    /**
     * 修改前状态
     */
    private String oldStatus;

    /**
     * 修改后状态
     */
    private String newStatus;

    /**
     * 司机id：若是APP操作修改，填写此处
     */
    private Integer driverId;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 轨迹描述
     */
    private String trackingInfo;

    /**
     * 发起修改设备序列号
     */
    private String device;

    /**
     * 修改人id：若是后台人员修改则填写此处
     */
    private Integer referUserId;

    /**
     * 运单状态修改记录时间
     */
    private Date createTime;

    /**
     * 跟踪类型：1-运输轨迹 2-提货补录 3-卸货补录 4-提货单据补录 5-卸货单据补录
     */
    private Integer trackingType;

    /**
     * 用户信息
     */
    private UserInfo userInfo;
    /**
     * 图片列表
     */
    private List<GetTrackingImagesDto> imageList;
}
