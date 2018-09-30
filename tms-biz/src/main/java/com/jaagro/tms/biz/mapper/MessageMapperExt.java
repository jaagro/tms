package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Message;

import java.util.List;

public interface MessageMapperExt extends  MessageMapper{

   List<Message> listMessageByCondtion(Message message);
}
