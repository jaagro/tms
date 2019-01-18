package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.ocr.WaybillOcrDto;

/**
 * @author tonyZheng
 * @date 2019-01-15 08:58
 */
public interface OcrService {

    /**
     * 基于牧原APP截图的运单识别
     *
     * @return
     */
    WaybillOcrDto getOcrByMuYuanAppImage(int waybillId, String imageUrl);
}
