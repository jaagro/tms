package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.RequestSource;
import com.jaagro.tms.api.dto.peripheral.*;
import com.jaagro.tms.api.service.WashTruckService;
import com.jaagro.tms.biz.entity.WashTruckImage;
import com.jaagro.tms.biz.entity.WashTruckRecord;
import com.jaagro.tms.biz.mapper.WashTruckImageMapperExt;
import com.jaagro.tms.biz.mapper.WashTruckRecordMapperExt;
import com.jaagro.tms.biz.service.OssSignUrlClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 洗车服务
 *
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
    @Autowired
    private OssSignUrlClientService ossSignUrlClientService;

    /**
     * 创建洗车记录
     *
     * @param createWashTruckRecordDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createWashTruckRecord(CreateWashTruckRecordDto createWashTruckRecordDto) {
        WashTruckRecord record = new WashTruckRecord();
        BeanUtils.copyProperties(createWashTruckRecordDto, record);
        UserInfo currentUser = currentUserService.getCurrentUser();
        record.setCreateTime(new Date())
                .setCreateUserId(currentUser == null ? null : currentUser.getId())
                .setEnable(true);
        washTruckRecordMapperExt.insertSelective(record);
        List<CreateWashTruckImageDto> imageDtoList = createWashTruckRecordDto.getImageList();
        if (!CollectionUtils.isEmpty(imageDtoList)) {
            List<WashTruckImage> imageList = new ArrayList<>();
            for (CreateWashTruckImageDto imageDto : imageDtoList) {
                WashTruckImage image = new WashTruckImage();
                BeanUtils.copyProperties(imageDto, image);
                image.setCreateTime(new Date())
                        .setCreateUserId(currentUser == null ? null : currentUser.getId())
                        .setWashTruckRecordId(record.getId());
                imageList.add(image);
            }
            washTruckImageMapperExt.batchInsert(imageList);
        }
    }

    /**
     * 洗车记录列表
     *
     * @param criteria
     * @return
     */
    @Override
    public PageInfo listWashTruckRecordByCriteria(ListWashTruckRecordCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        //去除车牌号中首尾空格
        criteria.setTruckNumber(StringUtils.hasText(criteria.getTruckNumber()) ? criteria.getTruckNumber().trim() : criteria.getTruckNumber());
        if (RequestSource.APP.equals(criteria.getRequestSource())) {
            UserInfo currentUser = currentUserService.getCurrentUser();
            Integer currentUserId = currentUser == null ? null : currentUser.getId();
            if (currentUserId == null) {
                throw new RuntimeException("登录超时");
            }
            criteria.setDriverId(currentUserId);
        }
        List<WashTruckRecordDto> washTruckRecordList = washTruckRecordMapperExt.listWashTruckRecordByCriteria(criteria);
        return new PageInfo(washTruckRecordList);
    }

    /**
     * 根据id查询洗车详情
     *
     * @param id
     * @return
     */
    @Override
    public WashTruckRecordDto getById(Integer id) {
        WashTruckRecordDto washTruckRecordDto = washTruckRecordMapperExt.selectById(id);
        if (washTruckRecordDto != null) {
            List<WashTruckImageDto> washTruckImageDtoList = washTruckRecordDto.getWashTruckImageDtoList();
            if (!CollectionUtils.isEmpty(washTruckImageDtoList)) {
                washTruckImageDtoList.forEach(imageDto -> convertImageUrl(imageDto));
            }
        }
        return washTruckRecordDto;
    }

    private void convertImageUrl(WashTruckImageDto imageDto) {
        if (imageDto != null && StringUtils.hasText(imageDto.getImageUrl())) {
            String[] strArray = {imageDto.getImageUrl()};
            List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray);
            if (!CollectionUtils.isEmpty(urls)) {
                imageDto.setImageUrl(urls.get(0).toString());
            }
        }
    }
}
