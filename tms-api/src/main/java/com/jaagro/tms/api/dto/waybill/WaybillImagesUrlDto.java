package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author gaoxin
 */
@Data
@Accessors(chain = true)
public class WaybillImagesUrlDto implements Serializable {

    /**
     * 区分 磅单 出库 签收单 图片类型
     */
    private Integer imagesType;
    /**
     * 图片地址相对路径
     */
    private String imagesUrl;
}
