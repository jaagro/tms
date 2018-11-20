package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.anomaly.AnomalyImageUrlDto;
import com.jaagro.tms.api.dto.anomaly.WaybillAnomalyImageCondition;
import com.jaagro.tms.biz.entity.WaybillAnomalyImage;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillAnomalyImageMapperExt extends WaybillAnomalyImageMapper {

    /**
     * 根据条件查询图片信息
     *
     * @param waybillAnomalyImageCondition
     * @return
     */
    List<WaybillAnomalyImage> listWaybillAnomalyImageByCondition(WaybillAnomalyImageCondition waybillAnomalyImageCondition);

    /**
     * 根据条件删除图片信息
     *
     * @param waybillAnomalyImageCondition
     */
    void deleteAnomalyImageByCondition(WaybillAnomalyImageCondition waybillAnomalyImageCondition);

}