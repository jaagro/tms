package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.message.CreateMessageDto;
import com.jaagro.tms.api.dto.message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.message.ListUnReadMsgCriteriaDto;
import com.jaagro.tms.api.dto.message.MessageReturnDto;

import java.util.List;

/**
 * 消息服务
 * @author yj
 * @date 2018/10/29
 */
public interface MessageService {
    /**
     * 分页查询消息
     * @param criteriaDto
     * @return
     */
    PageInfo<MessageReturnDto> listMessageByCriteriaDto(ListMessageCriteriaDto criteriaDto);

    /**
     * 将消息置为已读
     * @param messageIdList
     * @return
     */
    boolean readMessages(List<Integer> messageIdList);

    /**
     * 获取未读消息列表
     * @param criteriaDto
     * @return
     */
    List<MessageReturnDto> listUnreadMessages(ListUnReadMsgCriteriaDto criteriaDto);

    /**
     * 创建消息
     * @param createMessageDto
     * @return
     */
    boolean createMessage(CreateMessageDto createMessageDto);
}
