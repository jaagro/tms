package com.jaagro.tms.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.peripheral.CreateWashTruckImageDto;
import com.jaagro.tms.api.dto.peripheral.CreateWashTruckRecordDto;
import com.jaagro.tms.api.service.WashTruckService;
import com.jaagro.tms.biz.entity.WashTruckImage;
import com.jaagro.tms.biz.entity.WashTruckRecord;
import com.jaagro.tms.biz.entity.WaybillTrackingImages;
import com.jaagro.tms.biz.mapper.WashTruckImageMapperExt;
import com.jaagro.tms.biz.mapper.WashTruckRecordMapperExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 洗车服务
 * @author yj
 * @since 2018/12/10
 */
@Service
@Slf4j
public class WashTruckServiceImpl implements WashTruckService {
    @Autowired
    private WashTruckRecordMapperExt washTruckRecordMapperExt;
    @Autowired
    private WashTruckImageMapperExt washTruckImageMapperExt;
    @Autowired
    private CurrentUserService currentUserService;
    /**
     * 创建洗车记录
     * @param createWashTruckRecordDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createWashTruckRecord(CreateWashTruckRecordDto createWashTruckRecordDto) {
        log.info("O createWashTruckRecord {}",createWashTruckRecordDto);
        WashTruckRecord record = new WashTruckRecord();
        BeanUtils.copyProperties(createWashTruckRecordDto,record);
        UserInfo currentUser = currentUserService.getCurrentUser();
        record.setCreateTime(new Date())
                .setCreateUserId(currentUser == null ? null : currentUser.getId())
                .setEnable(true);
        washTruckRecordMapperExt.insertSelective(record);
        List<CreateWashTruckImageDto> imageDtoList = createWashTruckRecordDto.getImageList();
        if (!CollectionUtils.isEmpty(imageDtoList)){
            List<WashTruckImage> imageList = new ArrayList<>();
            for (CreateWashTruckImageDto imageDto : imageDtoList){
                WashTruckImage image = new WashTruckImage();
                BeanUtils.copyProperties(imageDto,image);
                image.setCreateTime(new Date())
                        .setCreateUserId(currentUser == null ? null : currentUser.getId())
                        .setWashTruckRecordId(record.getId());
                imageList.add(image);
            }
            washTruckImageMapperExt.batchInsert(imageList);
        }
    }
}
