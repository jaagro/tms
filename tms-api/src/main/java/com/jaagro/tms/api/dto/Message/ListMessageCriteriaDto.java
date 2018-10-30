package com.jaagro.tms.api.dto.Message;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分页查询消息条件
 * @author yj
 * @date 2018/10/29
 */
@Data
@Accessors(chain = true)
public class ListMessageCriteriaDto implements Serializable{
    /**
     * 当前页
     */
    @NotNull(message = "{pageNum.NotNull}")
    @Min(value = 1,message = "{pageNum.Min}")
    private Integer pageNum;

    /**
     * 每页的数量
     */
    @NotNull(message = "{pageSize.NotNull}")
    @Min(value = 1,message = "{pageSize.Min}")
    private Integer pageSize;

    /**
     * 消息类型：1-系统通知(公告) 2-运单相关 3-账务相关
     */
    @Min(value = 1,message = "{msgType.Min}")
    private Integer msgType;

    /**
     * 消息来源:1-APP,2-小程序,3-站内
     */
    @NotNull(message = "{msgSource.NotNull}")
    @Min(value = 1,message = "{msgSource.Min}")
    private Integer msgSource;

    /**
     * 消息接受者id：0-系统
     */
    private Integer toUserId;

    /**
     * 消息状态：0-未读 1-已读
     */
    private Integer msgStatus;
}
