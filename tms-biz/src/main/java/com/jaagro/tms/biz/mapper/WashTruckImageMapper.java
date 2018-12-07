package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WashTruckImage;

/**
 * @author yj
 * @since 20181207
 */
public interface WashTruckImageMapper {
    /**
     *
     * @mbggenerated 2018-12-07
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-07
     */
    int insert(WashTruckImage record);

    /**
     *
     * @mbggenerated 2018-12-07
     */
    int insertSelective(WashTruckImage record);

    /**
     *
     * @mbggenerated 2018-12-07
     */
    WashTruckImage selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-07
     */
    int updateByPrimaryKeySelective(WashTruckImage record);

    /**
     *
     * @mbggenerated 2018-12-07
     */
    int updateByPrimaryKey(WashTruckImage record);
}