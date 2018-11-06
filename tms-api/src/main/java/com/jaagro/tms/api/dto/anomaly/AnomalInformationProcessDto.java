package com.jaagro.tms.api.dto.anomaly;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class AnomalInformationProcessDto implements Serializable {

    /**
     * 异常Id
     */
    private Integer anomalId;

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 处理状态
     */
    private String processingStatus;

    /**
     * 处理描述
     */
    private String processingDesc;

    /**
     * 情况是否属实 0-否 1-是
     */
    private Boolean verifiedStatus;

    /**
     * 是否涉及费用调整  0-否 1-是
     */
    private Boolean adjustStatus;

    /**
     * 异常处理图片上传路径
     */
    private List<String> imagesUrl;

    /**
     * 是否涉及费用调整
     */
    private List<AnomalDeductCompensationDto> feeAdjust;

}
