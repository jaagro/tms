package com.jaagro.tms.web.vo;

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
public class MessageVo implements Serializable {

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

}
