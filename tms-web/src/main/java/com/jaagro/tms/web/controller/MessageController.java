package com.jaagro.tms.web.controller;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.Message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.Message.MessageReturnDto;
import com.jaagro.tms.api.service.MessageService;
import com.jaagro.tms.web.vo.MessageVo;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    public BaseResponse listMessages(@RequestBody ListMessageCriteriaDto criteriaDto) {
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
        List<MessageReturnDto> list = messageReturnDtoPageInfo.getList();
        if (CollectionUtils.isEmpty(list)){
            return BaseResponse.successInstance(messageReturnDtoPageInfo);
        }
        List<MessageVo> messageVos = new ArrayList<MessageVo>();
        for (MessageReturnDto dto : list){
            MessageVo messageVo = new MessageVo();
            BeanUtils.copyProperties(dto,messageVo);
            messageVos.add(messageVo);
        }
        return BaseResponse.successInstance(new PageInfo<MessageVo>(messageVos));
    }

}
