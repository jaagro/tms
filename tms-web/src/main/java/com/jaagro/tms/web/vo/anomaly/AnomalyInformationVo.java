package com.jaagro.tms.web.vo.anomaly;

import com.jaagro.tms.api.dto.anomaly.AnomalyDeductCompensationDto;
import com.jaagro.tms.api.dto.anomaly.AnomalyImageUrlDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Author @Gao.
 * 申报新增异常消息显示
 */
@Data
@Accessors(chain = true)
public class AnomalyInformationVo implements Serializable {

    /**
     * 运单id
     */
    private Integer waybillId;

    /**
     * 异常类型名称
     */
    private String typeName;

    /**
     * 异常描述
     */
    private String anomalyDesc;

    /**
     * 发货客户名称
     */
    private String customerName;

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 车牌号
     */

    private String truckNumber;

    /**
     * 异常图片
     */
    private List<AnomalyImageUrlDto> createAnomalyImageUrlDtos;

    // 处理结果字段
    /**
     * 情况是否属实 0-否 1-是
     */
    private Boolean verifiedStatus;

    /**
     * 是否涉及费用调整  0-否 1-是
     */
    private Boolean adjustStatus;

    /**
     *审核状态
     */
    private String auditStatus;

    /**
     * 审核描述
     */
    private String auditDesc;

    /**
     * 是否涉及费用调整 扣款对象 补偿对象
     */
    private List<AnomalyDeductCompensationDto> anomalyDeductCompensationDto;

    /**
     * 处理描述
     */
    private String processingDesc;

    /**
     * 处理者 上传的图片路径
     */
    private List<AnomalyImageUrlDto> processAnomalyImageUrlDtos;
}
