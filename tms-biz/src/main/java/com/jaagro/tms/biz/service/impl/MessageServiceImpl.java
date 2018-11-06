package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.Message.CreateMessageDto;
import com.jaagro.tms.api.dto.Message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.Message.ListUnReadMsgCriteriaDto;
import com.jaagro.tms.api.dto.Message.MessageReturnDto;
import com.jaagro.tms.api.service.MessageService;
import com.jaagro.tms.biz.entity.Message;
import com.jaagro.tms.biz.mapper.MessageMapperExt;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yj
 * @date 2018/10/29
 */
@Service
@Log4j
//@CacheConfig(keyGenerator = "wiselyKeyGenerator", cacheNames = "message")
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapperExt messageMapperExt;
    @Autowired
    private CurrentUserService currentUserService;
    /**
     * 分页查询消息
     *
     * @param criteriaDto
     * @return
     */
    //@Cacheable
    @Override
    public PageInfo<MessageReturnDto> listMessageByCriteriaDto(ListMessageCriteriaDto criteriaDto) {
        PageHelper.startPage(criteriaDto.getPageNum(),criteriaDto.getPageSize());
        criteriaDto.setToUserId(currentUserService.getCurrentUser() == null ? null : currentUserService.getCurrentUser().getId());
        List<MessageReturnDto> messageList = messageMapperExt.listMessageByCriteriaDto(criteriaDto);
        for(MessageReturnDto messageReturnDto : messageList){
            if (messageReturnDto.getMsgType() == 1){
                messageReturnDto.setMsgCategory(2);
            }else{
                messageReturnDto.setMsgCategory(1);
            }
        }
        return new PageInfo<MessageReturnDto>(messageList);
    }

    /**
     * 将消息置为已读
     *
     * @param messageIdList
     * @return
     */
    //@CacheEvict(cacheNames = "message", allEntries = true)
    @Override
    public boolean readMessages(List<Integer> messageIdList) {
        Integer currentUserId = currentUserService.getCurrentUser() == null ? null : currentUserService.getCurrentUser().getId();
        Integer successNum = messageMapperExt.readMessages(messageIdList,currentUserId);
        if (messageIdList.size() == successNum){
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
    //@Cacheable
    @Override
    public List<MessageReturnDto> listUnreadMessages(ListUnReadMsgCriteriaDto criteriaDto) {
        if (criteriaDto.getMsgStatus() == null){
            // 消息状态: 0-未读,1-已读
            criteriaDto.setMsgStatus(0);
        }
        UserInfo currentUser = currentUserService.getCurrentUser();
        Integer currentUserId = currentUser == null ? null : currentUser.getId();
        criteriaDto.setToUserId(currentUserId);
        ListMessageCriteriaDto dto = new ListMessageCriteriaDto();
        BeanUtils.copyProperties(criteriaDto,dto);
        List<MessageReturnDto> messageReturnDtos = messageMapperExt.listMessageByCriteriaDto(dto);
        for(MessageReturnDto messageReturnDto : messageReturnDtos){
            if (messageReturnDto.getMsgType() == 1){
                messageReturnDto.setMsgCategory(2);
            }else{
                messageReturnDto.setMsgCategory(1);
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
    //@CacheEvict(cacheNames = "message", allEntries = true)
    @Override
    public boolean createMessage(CreateMessageDto createMessageDto) {
        Message message = new Message();
        BeanUtils.copyProperties(createMessageDto,message);
        message.setCreateTime(new Date());
        UserInfo currentUser = currentUserService.getCurrentUser();
        Integer currentUserId = currentUser == null ? null : currentUser.getId();
        message.setCreateUserId(currentUserId);
        message.setEnabled(true);
        messageMapperExt.insertSelective(message);
        if (message.getId() != null && message.getId() > 0){
            return true;
        }
        return false;
    }
}
