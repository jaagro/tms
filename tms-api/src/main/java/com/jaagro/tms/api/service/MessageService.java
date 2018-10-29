package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.Message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.Message.MessageReturnDto;

import java.util.List;

/**
 * @author yj
 * @date 2018/10/29
 */
public interface MessageService {
    /**
     * 分页查询消息
     * @param criteriaDto
     * @return
     */
    List<MessageReturnDto> listMessageByCriteriaDto(ListMessageCriteriaDto criteriaDto);
}
