package com.jaagro.tms.api.dto.Message;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建消息参数
 *
 * @author yj
 * @date 2018/10/31
 */
@Data
@Accessors(chain = true)
public class CreateMessageDto implements Serializable {

    /**
     * 消息发送者id：0-系统
     */
    @NotNull(message = "{fromUserId.NotNull}")
    private Integer fromUserId;

    /**
     * 消息发送者类型：0-系统 1-客户 2-司机 3-员工
     */
    @NotNull(message = "{fromUserType.NotNull}")
    private Integer fromUserType;

    /**
     * 消息接受者id：0-系统
     */
    @NotNull(message = "{toUserId.NotNull}")
    private Integer toUserId;

    /**
     * 消息接受者类型：0-系统 1-客户 2-司机 3-员工
     */
    @NotNull(message = "{toUserType.NotNull}")
    private Integer toUserType;

    /**
     * 消息头
     */
    @NotBlank(message = "header.NotBlank")
    private String header;

    /**
     * 消息体
     */
    @NotBlank(message = "body.NotBlank")
    private String body;

    /**
     * 分类 1-通知,2-提醒
     */
    @NotNull(message = "{msgType.NotNull}")
    @Min(value = 1, message = "{msgType.Min}")
    private Integer category;

    /**
     * 消息类型：1-系统通知(公告) 2-运单相关 3-账务相关
     */
    @NotNull(message = "{msgType.NotNull}")
    @Min(value = 1, message = "{msgType.Min}")
    private Integer msgType;

    /**
     * 拒单类型：1-自动拒单  2-手动拒单(运单相关情况下会有数据)
     */
    @NotNull(message = "{msgType.NotNull}")
    @Min(value = 1, message = "{msgType.Min}")
    private Integer refuseType;

    /**
     * 消息来源:1-APP,2-小程序,3-站内
     */
    @NotNull(message = "{msgSource.NotNull}")
    @Min(value = 1, message = "{msgSource.Min}")
    private Integer msgSource;

    /**
     * 关联id
     */
    @Min(value = 1, message = "{referId.Min}")
    private Integer referId;

    /**
     * 创建人
     */
    @Min(value = 1, message = "{createUserId.Min}")
    private Integer createUserId;
}
