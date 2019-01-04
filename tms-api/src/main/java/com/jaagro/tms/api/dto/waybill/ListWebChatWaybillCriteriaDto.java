package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author baiyiran
 */
@Data
@Accessors(chain = true)
public class ListWebChatWaybillCriteriaDto implements Serializable {

    /**
     * 起始页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 运单状态：运输中、已完成，已取消
     */
    private String waybillStatus;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户类型
     */
    private Integer userType;

}
