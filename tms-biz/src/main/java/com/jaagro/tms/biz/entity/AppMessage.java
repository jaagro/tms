package com.jaagro.tms.biz.entity;

import java.util.Date;

public class AppMessage {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer truckId;

    /**
     * 消息类型：1-运单 9-系统
     */
    private Integer msgType;

    /**
     * 关联id：
     */
    private Integer waybillId;

    /**
     * 消息状态:0-未读 1;已读
     */
    private Integer msgStatus;

    /**
     * 消息头
     */
    private String header;

    /**
     * 消息体
     */
    private String body;

    /**
     * 消息开始时间
     */
    private Date startTime;

    /**
     * 
     */
    private Date endTime;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return truck_id 
     */
    public Integer getTruckId() {
        return truckId;
    }

    /**
     * 
     * @param truckId 
     */
    public void setTruckId(Integer truckId) {
        this.truckId = truckId;
    }

    /**
     * 消息类型：1-运单 9-系统
     * @return msg_type 消息类型：1-运单 9-系统
     */
    public Integer getMsgType() {
        return msgType;
    }

    /**
     * 消息类型：1-运单 9-系统
     * @param msgType 消息类型：1-运单 9-系统
     */
    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    /**
     * 关联id：
     * @return waybill_id 关联id：
     */
    public Integer getWaybillId() {
        return waybillId;
    }

    /**
     * 关联id：
     * @param waybillId 关联id：
     */
    public void setWaybillId(Integer waybillId) {
        this.waybillId = waybillId;
    }

    /**
     * 消息状态:0-未读 1;已读
     * @return msg_status 消息状态:0-未读 1;已读
     */
    public Integer getMsgStatus() {
        return msgStatus;
    }

    /**
     * 消息状态:0-未读 1;已读
     * @param msgStatus 消息状态:0-未读 1;已读
     */
    public void setMsgStatus(Integer msgStatus) {
        this.msgStatus = msgStatus;
    }

    /**
     * 消息头
     * @return header 消息头
     */
    public String getHeader() {
        return header;
    }

    /**
     * 消息头
     * @param header 消息头
     */
    public void setHeader(String header) {
        this.header = header == null ? null : header.trim();
    }

    /**
     * 消息体
     * @return body 消息体
     */
    public String getBody() {
        return body;
    }

    /**
     * 消息体
     * @param body 消息体
     */
    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    /**
     * 消息开始时间
     * @return start_time 消息开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 消息开始时间
     * @param startTime 消息开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 
     * @return end_time 
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 
     * @param endTime 
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}