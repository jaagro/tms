package com.jaagro.tms.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class Message implements Serializable {
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
     * 分类 1-通知,2-提醒
     */
    private Integer category;

    /**
     * 消息类型：1-系统通知(公告) 2-运单相关 3-账务相关4-运单磅单异常5-证件过期提醒
     */
    private Integer msgType;

    /**
     * 拒单类型：1-自动拒单  2-手动拒单(运单相关情况下会有数据)
     */
    private Integer refuseType;

    /**
     * 消息来源:1-APP,2-小程序,3-站内
     */
    private Integer msgSource;

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