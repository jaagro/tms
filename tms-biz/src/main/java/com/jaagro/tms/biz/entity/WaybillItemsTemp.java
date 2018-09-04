package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class WaybillItemsTemp implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer waybillTempId;}