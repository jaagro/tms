package com.jaagro.tms.api.dto.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author Gavin
 */
@Data
@Accessors(chain = true)
public class ListRepairRecordCriteriaDto implements Serializable {

    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 每页的数量
     */
    private int pageSize;

    /**
     * 车牌
     */
    private String truckNumber;
}
