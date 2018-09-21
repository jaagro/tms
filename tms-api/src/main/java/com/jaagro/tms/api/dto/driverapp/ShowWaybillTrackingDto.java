package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
/**
 * @author @gao
 */
@Data
@Accessors(chain = true)
public class ShowWaybillTrackingDto implements Serializable {
    /**
     * 用于前段封装
     */
    List<ShowTrackingDto> showTrackingDtos;
}
