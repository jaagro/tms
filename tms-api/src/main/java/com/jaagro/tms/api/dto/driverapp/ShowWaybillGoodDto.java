package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class ShowWaybillGoodDto implements Serializable{
    /**
     * 用于前段封装
     */
    private List<ShowGoodsDto> showGoodsDtos;

}
