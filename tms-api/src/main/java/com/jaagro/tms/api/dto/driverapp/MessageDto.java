package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class MessageDto implements Serializable{
    /**
     *
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
     *
     */
    private Date createTime;

    /**
     *
     */
    private Integer createUserId;

    /**
     *
     */
    private Date modifyTime;

    /**
     *
     */
    private Integer modifyUserId;
}
