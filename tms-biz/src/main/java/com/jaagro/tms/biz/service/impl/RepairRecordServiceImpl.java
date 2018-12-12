package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.peripheral.ListRepairRecordCriteriaDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.entity.RepairRecord;
import com.jaagro.tms.api.service.RepairRecordService;
import com.jaagro.tms.biz.mapper.RepairRecordMapperExt;
import com.jaagro.tms.biz.service.TruckClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Gavin
 * @Date 20181206
 */
@Service
public class RepairRecordServiceImpl implements RepairRecordService {

    @Autowired
    private RepairRecordMapperExt repairRecordMapper;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private TruckClientService truckClientService;


    /**
     * 新增维修记录
     *
     * @param record
     * @return
     */
    @Override
    public int createRepairRecord(RepairRecord record) {

        UserInfo userInfo = currentUserService.getCurrentUser();
        Integer userId = null == userInfo ? null : userInfo.getId();
        record.setCreateUserId(userId);
        //根据司机id获取该司机的车辆
        ShowTruckDto truckDto = truckClientService.getTruckByToken();
        if(null!=truckDto)
        {
            record.setTruckId(truckDto.getId())
                    .setTruckNumber(truckDto.getTruckNumber())
                    .setTruckTeamId(truckDto.getTruckTeamId())
                    .setDriverName(userInfo.getName())
                    .setDriverId(truckDto.getDrivers().get(0).getId());

        }
        return repairRecordMapper.insertSelective(record);
    }

    /**
     * 维修记录详情
     *
     * @param id
     * @mbggenerated 2018-12-06
     */
    @Override
    public RepairRecord getRepairRecordById(Integer id) {
        return repairRecordMapper.selectByPrimaryKey(id);
    }

    /**
     * 维修记录列表
     *
     * @param criteriaDto
     * @return
     */
    @Override
    public PageInfo<RepairRecord> listRepairRecordByCriteria(ListRepairRecordCriteriaDto criteriaDto) {
        PageHelper.startPage(criteriaDto.getPageNum(), criteriaDto.getPageSize());
        List<RepairRecord> repairRecordList  = repairRecordMapper.listRepairRecordByCondition(criteriaDto);
        return new PageInfo(repairRecordList);
    }
}
