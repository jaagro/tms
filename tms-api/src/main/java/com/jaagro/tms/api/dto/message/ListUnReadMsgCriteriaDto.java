package com.jaagro.tms.api.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 查询未读消息条件
 * @author yj
 * @date 2018/10/30
 */
@Data
@Accessors(chain = true)
public class ListUnReadMsgCriteriaDto implements Serializable{
    /**
     * 消息类型：1-系统通知(公告) 2-运单相关 3-账务相关
     */
    @Min(value = 1,message = "{msgType.Min}")
    private Integer msgType;

    /**
     * 消息来源:1-APP,2-小程序,3-站内
     */
    @Min(value = 1,message = "{msgSource.Min}")
    private Integer msgSource;

    /**
     * 类别: 1-通知 2-公告
     */
    @Min(value=1,message="{msgCategory.Min}")
    private Integer msgCategory;

    /**
     * 消息接受者id：0-系统
     */
    private Integer toUserId;

    /**
     * 消息状态：0-未读 1-已读
     */
    private Integer msgStatus;
}
