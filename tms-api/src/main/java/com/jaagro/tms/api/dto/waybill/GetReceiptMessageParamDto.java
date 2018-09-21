package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class GetReceiptMessageParamDto implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 货车id
     */
    private Integer truckId;
    /**
     * 消息类型
     */
    private Integer msgType;
    /**
     * waybillId
     */
    private Integer waybillId;
    /**
     * 起始页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

}
