package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.MsgCategory;
import com.jaagro.tms.api.constant.MsgStatusConstant;
import com.jaagro.tms.api.constant.MsgType;
import com.jaagro.tms.api.dto.message.CreateMessageDto;
import com.jaagro.tms.api.dto.message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.message.ListUnReadMsgCriteriaDto;
import com.jaagro.tms.api.dto.message.MessageReturnDto;
import com.jaagro.tms.api.service.MessageService;
import com.jaagro.tms.biz.entity.Message;
import com.jaagro.tms.biz.mapper.MessageMapperExt;
import com.jaagro.tms.biz.mapper.WaybillTrackingMapperExt;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        UserInfo currentUser = currentUserService.getCurrentUser();
        Integer currentUserId = currentUser == null ? null : currentUser.getId();
        criteriaDto.setToUserId(currentUserId);
        List<MessageReturnDto> messageList = messageMapperExt.listMessageByCriteriaDto(criteriaDto);
        generateCategory(messageList);
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
        List<MessageReturnDto> messageReturnDtoList = messageMapperExt.listMessageByCriteriaDto(dto);
        generateCategory(messageReturnDtoList);
        return messageReturnDtoList;
    }

    private void generateCategory(List<MessageReturnDto> messageReturnDtoList){
        for (MessageReturnDto messageReturnDto : messageReturnDtoList) {
            if (StringUtils.isEmpty(messageReturnDto.getCategory())) {
                if (messageReturnDto.getMsgType().equals(MsgType.SYSTEM)) {
                    messageReturnDto.setCategory(MsgCategory.INFORM);
                    messageReturnDto.setMsgCategory(MsgCategory.INFORM);
                } else {
                    messageReturnDto.setCategory(MsgCategory.WARNING);
                    messageReturnDto.setMsgCategory(MsgCategory.WARNING);
                }
            }else {
                if (messageReturnDto.getMsgType().equals(MsgType.SYSTEM)) {
                    messageReturnDto.setMsgCategory(MsgCategory.INFORM);
                } else {
                    messageReturnDto.setMsgCategory(MsgCategory.WARNING);
                }
            }
        }
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
