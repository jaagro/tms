package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author @Gao.
 * 异常图片封装
 */
@Data
@Accessors(chain = true)
public class AnomalyImageUrlDto implements Serializable {

    /**
     * 异常图片id
     */
    private Integer anomalyImageId;
    /**
     * 异常id
     */
    private Integer anomalyId;
    /**
     * 异常图片创建人id
     */
    private Integer imageType;
    /**
     * 图片url
     */
    private List<ImageUrlDto> imageUrlDtos;
}
