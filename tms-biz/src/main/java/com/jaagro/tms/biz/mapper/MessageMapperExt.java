package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapperExt extends  MessageMapper{

   List<Message> listMessageByCondtion(Message message);

   /**
    * 运单撤回时删除发给司机的短信
    * @Author gavin
    * @param waybillId
    * @param driverIds
    * @return
    */
   int deleteMessage(@Param("waybillId") Integer waybillId,@Param("driverIds") List<Integer> driverIds);
}
