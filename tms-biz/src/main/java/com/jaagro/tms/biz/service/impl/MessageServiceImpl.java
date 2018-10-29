package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.Message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.Message.MessageReturnDto;
import com.jaagro.tms.api.service.MessageService;
import com.jaagro.tms.biz.entity.Message;
import com.jaagro.tms.biz.mapper.MessageMapperExt;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
    /**
     * 分页查询消息
     *
     * @param criteriaDto
     * @return
     */
    @Override
    public PageInfo<List<MessageReturnDto>>  listMessageByCriteriaDto(ListMessageCriteriaDto criteriaDto) {
        PageHelper.startPage(criteriaDto.getPageNum(),criteriaDto.getPageSize());
        List<Message> messageList = messageMapperExt.listMessageByCriteriaDto(criteriaDto);
        if (CollectionUtils.isEmpty(messageList)){
            return null;
        }
        List<MessageReturnDto> messageReturnDtoList = new ArrayList<>();
        for(Message message : messageList){
            MessageReturnDto dto = new MessageReturnDto();
            BeanUtils.copyProperties(message,dto);
            messageReturnDtoList.add(dto);
        }
        return new PageInfo<>(messageReturnDtoList);
    }
}
