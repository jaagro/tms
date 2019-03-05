package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class WaybillImageChangeParamDto implements Serializable {
    private static final long serialVersionUID = 4850537984455883640L;
    /**
     * 运单号id
     */
    private Integer waybillId;
    /**
     * 运单详情关联图片id
     */
    private Integer waybillImagesId;
    /**
     *
     */
    private String waybillImagesUrl;
}
