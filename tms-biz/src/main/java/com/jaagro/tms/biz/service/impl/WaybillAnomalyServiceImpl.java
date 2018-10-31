package com.jaagro.tms.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.anomaly.WaybillAbnomalTypeDto;
import com.jaagro.tms.api.dto.anomaly.WaybillAnomalyReportDto;
import com.jaagro.tms.api.service.WaybillAnomalyService;
import com.jaagro.tms.biz.entity.WaybillAnomaly;
import com.jaagro.tms.biz.entity.WaybillAnomalyImage;
import com.jaagro.tms.biz.entity.WaybillAnomalyType;
import com.jaagro.tms.biz.mapper.WaybillAnomalyImageMapperExt;
import com.jaagro.tms.biz.mapper.WaybillAnomalyMapperExt;
import com.jaagro.tms.biz.mapper.WaybillAnomalyTypeMapperExt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author @Gao.
 */
@Service
public class WaybillAnomalyServiceImpl implements WaybillAnomalyService {

    @Autowired
    private WaybillAnomalyMapperExt waybillAnomalyMapper;
    @Autowired
    private WaybillAnomalyTypeMapperExt waybillAnomalyTypeMapper;
    @Autowired
    private WaybillAnomalyImageMapperExt waybillAnomalyImageMapper;
    @Autowired
    private CurrentUserService currentUserService;

    /**
     * 司机运单异常申报
     * Author @Gao.
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void waybillAnomalyReport(WaybillAnomalyReportDto dto) {
        //插入异常表
        WaybillAnomaly waybillAnomaly = new WaybillAnomaly();
        BeanUtils.copyProperties(dto,waybillAnomaly);
        UserInfo currentUser = currentUserService.getCurrentUser();
        waybillAnomaly.setCreateUserId(currentUser.getId());
        waybillAnomalyMapper.insertSelective(waybillAnomaly);
        //插入异常图片表
        WaybillAnomalyImage waybillAnomalyImage = new WaybillAnomalyImage();
        waybillAnomalyImage
                .setAnomalyId(waybillAnomaly.getId())
                .setCreateUserId(currentUser.getId())
                .setWaybillId(dto.getWaybillId())
                .setImageUrl(dto.getImageUrl());
        waybillAnomalyImageMapper.insertSelective(waybillAnomalyImage);
    }

    /**
     * 运单异常类型显示
     * Author @Gao.
     */
    @Override
    public List<WaybillAbnomalTypeDto> displayAbnormalType() {
        List<WaybillAbnomalTypeDto> waybillAbnomalTypeDtos = waybillAnomalyTypeMapper.listAnomalyType();
        return waybillAbnomalTypeDtos;

    }
}
