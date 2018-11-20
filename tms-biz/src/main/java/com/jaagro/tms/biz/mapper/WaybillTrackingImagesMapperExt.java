package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.driverapp.GetWaybillTrackingImagesDto;
import com.jaagro.tms.biz.entity.WaybillTrackingImages;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillTrackingImagesMapperExt extends WaybillTrackingImagesMapper {

    /**
     * 根据卸货地 装货地id
     * @param record
     * @return
     */
    List<GetWaybillTrackingImagesDto> listWaybillTrackingImage(WaybillTrackingImages record);

    /**
     * 物理删除运单轨迹图片
     * @author yj
     * @param waybillId
     * @param imageType
     * @return
     */
    Integer deleteByWaybillIdAndImageType(@Param("waybillId") Integer waybillId, @Param("imageType") Integer imageType);

    /**
     * 批量插入运单轨迹图片
     * @author yj
     * @param imagesList
     * @return
     */
    Integer batchInsert(@Param("imagesList") List<WaybillTrackingImages> imagesList);
}