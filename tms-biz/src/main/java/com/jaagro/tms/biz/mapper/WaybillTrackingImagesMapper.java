package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillTrackingImages;

/**
 * @author gavin
 */

public interface WaybillTrackingImagesMapper {
    /**
     *
     * @mbggenerated 2018-10-12
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-12
     */
    int insert(WaybillTrackingImages record);

    /**
     *
     * @mbggenerated 2018-10-12
     */
    int insertSelective(WaybillTrackingImages record);

    /**
     *
     * @mbggenerated 2018-10-12
     */
    WaybillTrackingImages selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-12
     */
    int updateByPrimaryKeySelective(WaybillTrackingImages record);

    /**
     *
     * @mbggenerated 2018-10-12
     */
    int updateByPrimaryKey(WaybillTrackingImages record);
}