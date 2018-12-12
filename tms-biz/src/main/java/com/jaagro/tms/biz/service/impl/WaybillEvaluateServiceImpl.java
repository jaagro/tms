package com.jaagro.tms.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.waybill.CreateEvaluateTypeDto;
import com.jaagro.tms.api.dto.waybill.CreateWaybillEvaluateDto;
import com.jaagro.tms.api.dto.waybill.ListEvaluateTypeDto;
import com.jaagro.tms.api.service.WaybillEvaluateService;
import com.jaagro.tms.biz.entity.WaybillEvaluate;
import com.jaagro.tms.biz.mapper.EvaluateTypeMapperExt;
import com.jaagro.tms.biz.mapper.WaybillEvaluateMapperExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author @Gao.
 */
@Service
@Slf4j
public class WaybillEvaluateServiceImpl implements WaybillEvaluateService {
    @Autowired
    private WaybillEvaluateMapperExt waybillEvaluateMapper;
    @Autowired
    private EvaluateTypeMapperExt evaluateTypeMapper;
    @Autowired
    private CurrentUserService currentUserService;

    /**
     * 根据满意度等级 查询对应的描述
     *
     * @param lever
     * @return
     */
    @Override
    public List<ListEvaluateTypeDto> listEvaluateType(Integer lever) {
        return evaluateTypeMapper.listEvaluateType(lever);
    }

    /**
     * 保存运单评价数据
     *
     * @param dto
     */
    @Override
    public void createWaybillEvaluate(CreateWaybillEvaluateDto dto) {
        WaybillEvaluate waybillEvaluate = new WaybillEvaluate();
        UserInfo currentUser = currentUserService.getCurrentUser();
        if (null != currentUser && null != dto.getCreateEvaluateType()) {
            List<CreateEvaluateTypeDto> createEvaluateType = dto.getCreateEvaluateType();
            String satisfactionLeverDesc = JSON.toJSONString(createEvaluateType);
            if (!CollectionUtils.isEmpty(createEvaluateType)) {
                waybillEvaluate
                        .setNote(dto.getNote() == null ? null : dto.getNote())
                        .setSatisfactionLeverDesc(satisfactionLeverDesc)
                        .setCreateUserId(currentUser.getId())
                        .setSatisfactionLever(createEvaluateType.get(0).getSatisfactionLever())
                        .setDriverId(dto.getDriverId() == null ? null : dto.getDriverId())
                        .setWaybillId(dto.getWaybillId() == null ? null : dto.getWaybillId())
                        .setCreateUserId(currentUser.getId());
            }
        }
        waybillEvaluateMapper.insert(waybillEvaluate);
    }
}
