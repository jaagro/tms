package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.anomaly.WaybillAnomalyImageCondtion;
import com.jaagro.tms.biz.entity.WaybillAnomalyImage;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillAnomalyImageMapperExt extends WaybillAnomalyImageMapper {

   List<WaybillAnomalyImage> listWaybillAnormalyImageByCondtion(WaybillAnomalyImageCondtion waybillAnomalyImageCondtion);

}