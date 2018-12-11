package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.peripheral.WashTruckImageDto;
import com.jaagro.tms.biz.entity.WashTruckImage;
import org.apache.ibatis.annotations.Param;

import java.util.List; /**
 * @author yj
 * @since 2018/12/7
 */
public interface WashTruckImageMapperExt extends WashTruckImageMapper {
    /**
     * 批量插入
     * @param imageList
     * @return
     */
    Integer batchInsert(@Param("imageList") List<WashTruckImage> imageList);

    /**
     * 根据洗车记录id查询
     * @param washTruckRecordId
     * @return
     */
    List<WashTruckImageDto> listByWashTruckRecordId(@Param("washTruckRecordId") Integer washTruckRecordId);
}
