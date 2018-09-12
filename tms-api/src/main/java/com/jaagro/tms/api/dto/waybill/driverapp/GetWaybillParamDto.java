package com.jaagro.tms.api.dto.waybill.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @gao
 */
@Data
@Accessors(chain = true)
public class GetWaybillParamDto implements Serializable {
    /**
     * 起始页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 承运中 已完成 已取消 参数
     */
    private String waybillStatus;


}
