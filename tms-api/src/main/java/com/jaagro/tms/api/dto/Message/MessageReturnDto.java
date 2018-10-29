package com.jaagro.tms.api.dto.Message;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息返回
 * @author yj
 * @date 2018/10/29
 */
@Data
@Accessors(chain = true)
public class MessageReturnDto implements Serializable{
    /**
     * 消息id
     */
    private Integer id;

    /**
     * 消息发送者id：0-系统
     */
    private Integer fromUserId;

    /**
     * 消息发送者类型：0-系统
     */
    private Integer fromUserType;

    /**
     * 消息接受者id：0-系统
     */
    private Integer toUserId;

    /**
     * 消息接受者类型：0-系统
     */
    private Integer toUserType;

    /**
     * 消息头
     */
    private String header;

    /**
     * 消息体
     */
    private String body;

    /**
     * 消息类型：1-系统通知 2-运单相关 3-账务相关
     */
    private Integer msgType;

    /**
     * 消息类别：1-通知 2-公告
     */
    private Integer type;

    /**
     * 关联id
     */
    private Integer referId;

    /**
     * 消息状态：0-未读 1-已读
     */
    private Integer msgStatus;

    /**
     * 是否有效：1-有效 0-无效
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人
     */
    private Integer modifyUserId;
}
