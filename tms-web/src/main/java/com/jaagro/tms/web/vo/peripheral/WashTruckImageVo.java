package com.jaagro.tms.web.vo.peripheral;

import java.io.Serializable;
import java.util.Date;

/**
 * 洗车图片
 * @author yj
 * @since 2018/12/11
 */
public class WashTruckImageVo implements Serializable{
    /**
     * 图片类型 1-车前,2-车中,3-车后
     */
    private Integer imageType;

    /**
     * 图片路径
     */
    private String imageUrl;
}
