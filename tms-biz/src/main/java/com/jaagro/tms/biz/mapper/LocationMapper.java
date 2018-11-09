package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Location;
import com.jaagro.tms.biz.entity.LocationExample;
import java.util.List;

public interface LocationMapper {
    /**
     *
     * @mbggenerated 2018-11-01
     */
    int countByExample(LocationExample example);

    /**
     *
     * @mbggenerated 2018-11-01
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-11-01
     */
    int insert(Location record);

    /**
     *
     * @mbggenerated 2018-11-01
     */
    int insertSelective(Location record);

    /**
     *
     * @mbggenerated 2018-11-01
     */
    List<Location> selectByExample(LocationExample example);

    /**
     *
     * @mbggenerated 2018-11-01
     */
    Location selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-11-01
     */
    int updateByPrimaryKeySelective(Location record);

    /**
     *
     * @mbggenerated 2018-11-01
     */
    int updateByPrimaryKey(Location record);
}