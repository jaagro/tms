package com.jaagro.tms.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.waybill.CreateEvaluateTypeDto;
import com.jaagro.tms.api.dto.waybill.CreateWaybillEvaluateDto;
import com.jaagro.tms.api.dto.waybill.ListEvaluateTypeDto;
import com.jaagro.tms.api.service.WaybillEvaluateService;
import com.jaagro.tms.biz.entity.Waybill;
import com.jaagro.tms.biz.entity.WaybillEvaluate;
import com.jaagro.tms.biz.mapper.EvaluateTypeMapperExt;
import com.jaagro.tms.biz.mapper.WaybillEvaluateMapperExt;
import com.jaagro.tms.biz.mapper.WaybillMapperExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private WaybillMapperExt waybillMapper;

    /**
     * 根据满意度等级 查询对应的描述
     *
     * @param
     * @return
     */
    @Override
    public List<ListEvaluateTypeDto> listEvaluateType() {
        return evaluateTypeMapper.listEvaluateType();
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
        Waybill waybill = waybillMapper.getWaybillById(dto.getWaybillId());
        List<CreateEvaluateTypeDto> createEvaluateType = dto.getCreateEvaluateType();
        waybillEvaluate
                .setWaybillId(dto.getWaybillId() == null ? null : dto.getWaybillId())
                .setDriverId(waybill.getDriverId() == null ? null : waybill.getDriverId())
                .setNote(dto.getNote() == null ? null : dto.getNote())
                .setCreateUserId(currentUser.getId() == null ? null : currentUser.getId());
        if (!CollectionUtils.isEmpty(createEvaluateType)) {
            String satisfactionLeverDesc = JSON.toJSONString(createEvaluateType);
            waybillEvaluate
                    .setSatisfactionLeverDesc(satisfactionLeverDesc)
                    .setSatisfactionLever(createEvaluateType.get(0).getSatisfactionLever());
        }
        waybillEvaluateMapper.insertSelective(waybillEvaluate);
    }

    /**
     * 根据运单id查询评价信息
     *
     * @param waybillId
     * @return
     */
    @Override
    public ListEvaluateTypeDto getWaybillEvaluateByWaybillId(Integer waybillId) {
        ListEvaluateTypeDto listEvaluateTypeDto = new ListEvaluateTypeDto();
        WaybillEvaluate waybillEvaluate = waybillEvaluateMapper.getWaybillEvaluateByWaybillId(waybillId);
        BeanUtils.copyProperties(waybillEvaluate, listEvaluateTypeDto);
        return listEvaluateTypeDto;
    }
}
