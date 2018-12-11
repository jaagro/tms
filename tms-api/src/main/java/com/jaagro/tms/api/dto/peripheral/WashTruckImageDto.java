package com.jaagro.tms.api.dto.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yj
 * @since 2018/12/11
 */
@Data
@Accessors(chain = true)
public class WashTruckImageDto implements Serializable {
    /**
     * 洗车图片表id
     */
    private Integer id;

    /**
     * 洗车记录id
     */
    private Integer washTruckRecordId;

    /**
     * 图片类型 1-车前,2-车中,3-车后
     */
    private Integer imageType;

    /**
     * 图片路径
     */
    private String imageUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private Integer createUserId;
}
