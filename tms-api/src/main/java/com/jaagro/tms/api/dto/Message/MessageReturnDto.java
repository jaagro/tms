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
     * 消息来源:1-APP,2-小程序,3-站内
     */
    private Integer msgSource;
    /**
     * 类别: 1-通知 2-公告
     */
    private Integer msgCategory;
    /**
     * 关联id
     */
    private Integer referId;

    /**
     * 消息状态：0-未读 1-已读
     */
    private Integer msgStatus;
    /**
     * 创建时间
     */
    private Date createTime;

}
