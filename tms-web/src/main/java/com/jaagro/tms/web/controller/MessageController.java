package com.jaagro.tms.web.controller;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.Message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.Message.ListUnReadMsgCriteriaDto;
import com.jaagro.tms.api.dto.Message.MessageReturnDto;
import com.jaagro.tms.api.service.MessageService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
        log.debug("listMessages,{}",criteriaDto);
        if (criteriaDto.getPageNum() == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageNum不能为空");
        }
        if (criteriaDto.getPageSize() == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageSize不能为空");
        }
        if (criteriaDto.getPageNum() == 0) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageNum不能为0");
        }
        if (criteriaDto.getPageSize() == 0) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageSize不能为0");
        }
        PageInfo<MessageReturnDto> messageReturnDtoPageInfo = messageService.listMessageByCriteriaDto(criteriaDto);
        return BaseResponse.successInstance(messageReturnDtoPageInfo);
    }

    @PostMapping("/readMessages")
    @ApiOperation("将消息置为已读")
    public BaseResponse readMessages(@RequestBody List<Integer> messageIdList){
        boolean success = messageService.readMessages(messageIdList);
        if (success){
            return BaseResponse.successInstance("已成功将消息置为已读");
        }
        return BaseResponse.errorInstance("将消息置为已读失败");
    }

    @PostMapping("/listUnreadMessages")
    @ApiOperation("获取未读消息")
    public BaseResponse listUnreadMessages(@RequestBody @Validated ListUnReadMsgCriteriaDto criteriaDto){
        if (criteriaDto.getMsgSource() == null){
            return BaseResponse.errorInstance("消息来源不能为空");
        }
        if (criteriaDto.getMsgSource() == 0){
            return BaseResponse.errorInstance("消息来源不能为0");
        }
        List<MessageReturnDto> messageReturnDtos = messageService.listUnreadMessages(criteriaDto);
        return BaseResponse.successInstance(messageReturnDtos);
    }
}
