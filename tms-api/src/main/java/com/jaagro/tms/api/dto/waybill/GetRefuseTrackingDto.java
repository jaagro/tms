package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 拒单理由
 *
 * @author baiyiran
 */
@Data
@Accessors(chain = true)
public class GetRefuseTrackingDto implements Serializable {

    /**
     * 修改后状态
     */
    private String newStatus;

    /**
     * 运单状态修改记录时间
     */
    private Date createTime;

    /**
     * 拒单理由id
     */
    private Integer refuseReasonId;

    /**
     * 拒单理由
     */
    private String refuseReason;

}
