package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.MsgCategory;
import com.jaagro.tms.api.constant.MsgStatusConstant;
import com.jaagro.tms.api.constant.MsgType;
import com.jaagro.tms.api.dto.Message.CreateMessageDto;
import com.jaagro.tms.api.dto.Message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.Message.ListUnReadMsgCriteriaDto;
import com.jaagro.tms.api.dto.Message.MessageReturnDto;
import com.jaagro.tms.api.service.MessageService;
import com.jaagro.tms.biz.entity.Message;
import com.jaagro.tms.biz.mapper.MessageMapperExt;
import com.jaagro.tms.biz.mapper.WaybillTrackingMapperExt;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yj
 * @date 2018/10/29
 */
@Service
@Log4j
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapperExt messageMapperExt;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private WaybillTrackingMapperExt trackingMapperExt;

    /**
     * 分页查询消息
     *
     * @param criteriaDto
     * @return
     */
    @Override
    public PageInfo<MessageReturnDto> listMessageByCriteriaDto(ListMessageCriteriaDto criteriaDto) {
        PageHelper.startPage(criteriaDto.getPageNum(), criteriaDto.getPageSize());
        criteriaDto.setToUserId(currentUserService.getCurrentUser() == null ? null : currentUserService.getCurrentUser().getId());
        List<MessageReturnDto> messageList = messageMapperExt.listMessageByCriteriaDto(criteriaDto);
        for (MessageReturnDto messageReturnDto : messageList) {
            if (StringUtils.isEmpty(messageReturnDto.getMsgCategory())) {

            }
            if (messageReturnDto.getMsgType().equals(MsgType.SYSTEM)) {
                messageReturnDto.setMsgCategory(MsgCategory.WARNING);
            } else {
                messageReturnDto.setMsgCategory(MsgCategory.INFORM);
            }
        }
        return new PageInfo<>(messageList);
    }

    /**
     * 将消息置为已读
     *
     * @param messageIdList
     * @return
     */
    @Override
    public boolean readMessages(List<Integer> messageIdList) {
        Integer currentUserId = currentUserService.getCurrentUser() == null ? null : currentUserService.getCurrentUser().getId();
        Integer successNum = messageMapperExt.readMessages(messageIdList, currentUserId);
        if (messageIdList.size() == successNum) {
            return true;
        }
        return false;
    }

    /**
     * 获取未读消息列表
     *
     * @param criteriaDto
     * @return
     */
    @Override
    public List<MessageReturnDto> listUnreadMessages(ListUnReadMsgCriteriaDto criteriaDto) {
        if (criteriaDto.getMsgStatus() == null) {
            // 消息状态: 0-未读,1-已读
            criteriaDto.setMsgStatus(MsgStatusConstant.UNREAD);
        }
        UserInfo currentUser = currentUserService.getCurrentUser();
        Integer currentUserId = currentUser == null ? null : currentUser.getId();
        criteriaDto.setToUserId(currentUserId);
        ListMessageCriteriaDto dto = new ListMessageCriteriaDto();
        BeanUtils.copyProperties(criteriaDto, dto);
        List<MessageReturnDto> messageReturnDtos = messageMapperExt.listMessageByCriteriaDto(dto);
        for (MessageReturnDto messageReturnDto : messageReturnDtos) {
            if (messageReturnDto.getMsgType().equals(MsgType.SYSTEM)) {
                messageReturnDto.setMsgCategory(MsgCategory.WARNING);
            } else {
                messageReturnDto.setMsgCategory(MsgCategory.INFORM);
            }
        }
        return messageReturnDtos;
    }

    /**
     * 创建消息
     *
     * @param createMessageDto
     * @return
     */
    @Override
    public boolean createMessage(CreateMessageDto createMessageDto) {
        Message message = new Message();
        BeanUtils.copyProperties(createMessageDto, message);
        message.setCreateTime(new Date());
        if (createMessageDto.getCreateUserId() == null) {
            UserInfo currentUser = currentUserService.getCurrentUser();
            Integer currentUserId = currentUser == null ? null : currentUser.getId();
            message.setCreateUserId(currentUserId);
        }
        message.setEnabled(true);
        //运单相关的消息
        if (!StringUtils.isEmpty(createMessageDto.getMsgType()) && MsgType.WAYBILL.equals(createMessageDto.getMsgType())) {
            trackingMapperExt.getWaybillTrackingByWaybillId(createMessageDto.getReferId());
        }
        messageMapperExt.insertSelective(message);
        if (message.getId() != null && message.getId() > 0) {
            return true;
        }
        return false;
    }
}
