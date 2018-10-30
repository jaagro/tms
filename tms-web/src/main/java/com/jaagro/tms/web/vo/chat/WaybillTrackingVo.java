package com.jaagro.tms.web.vo.chat;

import com.jaagro.tms.api.dto.waybill.GetTrackingImagesDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class WaybillTrackingVo implements Serializable {

    /**
     * 轨迹描述
     */
    private String trackingInfo;

    /**
     * 运单状态修改记录时间
     */
    private Date createTime;

    /**
     * 图片列表
     */
    private List<GetTrackingImagesDto> imageList;
}
