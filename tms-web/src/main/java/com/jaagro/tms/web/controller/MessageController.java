package com.jaagro.tms.web.controller;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.message.CreateMessageDto;
import com.jaagro.tms.api.dto.message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.message.ListUnReadMsgCriteriaDto;
import com.jaagro.tms.api.dto.message.MessageReturnDto;
import com.jaagro.tms.api.service.MessageService;
import com.jaagro.utils.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消息管理
 * @author yj
 * @date 2018/10/29
 */
@RestController
@Api(description = "消息管理", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MessageController {
    @Autowired
    private MessageService messageService;
    @PostMapping("/listMessages")
    @ApiOperation("消息列表")
    public BaseResponse listMessages(@RequestBody @Validated ListMessageCriteriaDto criteriaDto) {
        log.info("O listMessages,criteriaDto={}",criteriaDto);
        PageInfo<MessageReturnDto> messageReturnDtoPageInfo = messageService.listMessageByCriteriaDto(criteriaDto);
        return BaseResponse.successInstance(messageReturnDtoPageInfo);
    }

    @PostMapping("/readMessages")
    @ApiOperation("将消息置为已读")
    public BaseResponse readMessages(@RequestBody List<Integer> messageIdList){
        log.info("O readMessages,messageIdList={}",messageIdList);
        if (CollectionUtils.isEmpty(messageIdList)){
            return BaseResponse.errorInstance("消息id列表不能为空");
        }
        boolean success = messageService.readMessages(messageIdList);
        if (success){
            return BaseResponse.successInstance("已成功将消息置为已读");
        }
        return BaseResponse.errorInstance("将消息置为已读失败");
    }

    @PostMapping("/listUnreadMessages")
    @ApiOperation("获取未读消息")
    public BaseResponse listUnreadMessages(@RequestBody @Validated ListUnReadMsgCriteriaDto criteriaDto){
        log.info("O listUnreadMessages criteriaDto={}",criteriaDto);
        List<MessageReturnDto> messageReturnDtoList = messageService.listUnreadMessages(criteriaDto);
        return BaseResponse.successInstance(messageReturnDtoList);
    }

    @PostMapping("/createMessage")
    @ApiOperation("创建消息")
    public BaseResponse createMessage(@RequestBody @Validated CreateMessageDto createMessageDto){
        log.info("O createMessage,createMessageDto={}",createMessageDto);
        boolean success = messageService.createMessage(createMessageDto);
        if (success){
            return BaseResponse.successInstance("创建消息成功");
        }
        return BaseResponse.errorInstance("创建消息失败");
    }
}
