package com.jaagro.tms.web.vo.anomaly;

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
public class AnomalInformationVo implements Serializable {

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
     *
     * 发货客户名称
     */
    private String customerName;

    /**
     * 异常图片
     */
     List<String> imageUrl;
}
