package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.Message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.Message.MessageReturnDto;
import com.jaagro.tms.biz.entity.Message;

import java.util.List;

public interface MessageMapperExt extends  MessageMapper{
   /**
    * 根据条件查消息(app端)
    * @param message
    * @return
    */
   List<Message> listMessageByCondtion(Message message);

   /**
    * 根据条件查消息
    * @param criteriaDto
    * @return
    */
   List<MessageReturnDto> listMessageByCriteriaDto(ListMessageCriteriaDto criteriaDto);


   
}
