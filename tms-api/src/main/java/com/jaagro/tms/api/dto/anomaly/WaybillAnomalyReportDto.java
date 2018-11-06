package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class WaybillAnomalyReportDto implements Serializable {

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 异常类型id
     */
    private Integer anomalyTypeId;

    /**
     * 异常描述
     */
    private String anomalyDesc;

    /**
     * 创建人用户类型
     */
    private String createUserType;
    /**
     * 异常上传图片路径
     */
    private String imageUrl;
}
