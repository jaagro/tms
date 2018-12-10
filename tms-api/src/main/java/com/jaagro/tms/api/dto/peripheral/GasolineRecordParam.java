package com.jaagro.tms.api.dto.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class GasolineRecordParam {
    /**
     * 起始页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;
}
