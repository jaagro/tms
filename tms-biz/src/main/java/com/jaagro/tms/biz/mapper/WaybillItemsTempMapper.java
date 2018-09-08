package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillItemsTemp;
import org.springframework.stereotype.Repository;

@Repository
public interface WaybillItemsTempMapper {
    /**
     *
     * @mbggenerated 2018-09-04
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int insert(WaybillItemsTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int insertSelective(WaybillItemsTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    WaybillItemsTemp selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int updateByPrimaryKeySelective(WaybillItemsTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int updateByPrimaryKey(WaybillItemsTemp record);

    /**
     * 根据运单id和配送地Id查询
     * @param waybillTempId
     * @param unloadSiteId
     * @return
     */
    WaybillItemsTemp getByWaybillIdAndUnloadSiteId(Integer waybillTempId,Integer unloadSiteId);
}