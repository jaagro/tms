package com.jaagro.tms.web.vo.chat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author gavin
 * @Date 2018/10/31
 */
@Data
@Accessors(chain = true)
public class WaybillTrackingImagesVo implements Serializable {
    /**
     * 图片地址
     */
    private String imageUrl;
}
