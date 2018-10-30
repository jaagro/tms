package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.waybill.GetWaybillDetailDto;
import com.jaagro.tms.api.service.WaybillRefactorService;
import com.jaagro.tms.web.vo.chat.WaybillDetailVo;
import com.jaagro.utils.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gavin
 */
@RestController
@Api(description = "微信小程序运单", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebChatAppWaybillController {

    @Autowired
    private WaybillRefactorService waybillService;

    /**
     * @Author gavin
     * @param id
     * @return
     */

    @ApiOperation("通过id获取微信小程序运单详情")
    @GetMapping("/getWaybillDetailById/{id}")
    public BaseResponse getWaybillDetailById(@PathVariable("id") Integer id) {
        WaybillDetailVo detailVo = new WaybillDetailVo();
        try {
            GetWaybillDetailDto detailDto = waybillService.getWaybillDetailById(id);
            BeanUtils.copyProperties(detailDto,detailVo);

        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.errorInstance(e.getMessage());
        }
        return BaseResponse.successInstance(detailVo);
    }

}