package com.jaagro.tms.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tony
 */
@Data
public class CreateWaybillByExcelDto implements Serializable {

    private List<LoadExcelDto> waybills;
}
