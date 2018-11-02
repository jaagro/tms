package com.jaagro.tms.web.vo.receipt;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 运单轨迹图片
 * @author yj
 * @date 2018/10/31
 */
@Data
@Accessors(chain = true)
public class WaybillTrackingImagesVo implements Serializable{
    /**
     * 图片类型：0-出库单 1-磅单 2- 签收单 3-回单补录
     */
    private Integer imageType;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 上传时间
     */
    private Date createTime;

    /**
     * 上传人
     */
    private String createUserName;
}
