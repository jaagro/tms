package com.jaagro.tms.api.dto.Message;

import lombok.Data;
import lombok.experimental.Accessors;

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
    private Integer pageNum;

    /**
     * 每页的数量
     */
    private Integer pageSize;

    /**
     * 消息类型：1-系统通知 2-运单相关 3-账务相关
     */
    private Integer msgType;

    /**
     * 消息类别：1-通知 2-公告
     */
    private Integer type;

    /**
     * 消息接受者id：0-系统
     */
    private Integer toUserId;

    /**
     * 消息状态：0-未读 1-已读
     */
    private Integer msgStatus;
}
