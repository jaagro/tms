package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 * 用于分装相对路径 绝对路径
 */
@Data
@Accessors(chain = true)
public class ImageUrlDto implements Serializable {
    /**
     * 1--相对路径 2--绝对路径
     */
    private Integer key;
    /**
     * 图片路径
     */
    private String imagesUrl;

}
