package com.jaagro.tms.api.dto.ocr;

import lombok.Data;

import java.util.List;

/**
 * @author tonyZheng
 * @date 2019-01-14 18:27
 */
@Data
public class MuYuanAppImageDto {
    String code;
    String msg;
    List<MuYuanAppResultDto> result;
}
