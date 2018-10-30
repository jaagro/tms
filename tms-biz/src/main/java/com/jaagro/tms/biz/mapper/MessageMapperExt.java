package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.Message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.Message.MessageReturnDto;
import com.jaagro.tms.biz.entity.Message;
import org.apache.ibatis.annotations.Param;

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

   /**
    * 运单撤回时删除发给司机的短信
    * @Author gavin
    * @param waybillId
    * @param driverIds
    * @return
    */
   int deleteMessage(@Param("waybillId") Integer waybillId, @Param("driverIds") List<Integer> driverIds);

   /**
    * 将消息置为已读
    * @param messageIdList
    * @param  modifyUserId
    * @return
    */
   Integer readMessages(@Param("messageIdList") List<Integer> messageIdList,@Param("modifyUserId") Integer modifyUserId);
}
