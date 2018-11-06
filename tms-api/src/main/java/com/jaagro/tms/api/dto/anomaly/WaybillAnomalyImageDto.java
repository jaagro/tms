package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class WaybillAnomalyImageDto implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 异常Id
     */
    private Integer anomalyId;

    /**
     * 图片类型
     */
    private Integer imageType;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 逻辑删除
     */
    private Boolean enabled;
}
