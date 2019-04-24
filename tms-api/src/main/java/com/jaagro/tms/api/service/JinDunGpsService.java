package com.jaagro.tms.api.service;

/**
 * 金盾服务
 */
public interface JinDunGpsService {

    /***
     * 金盾系统登录
     * @return
     */
    String jinDunLogin(boolean expiresType);

    /**
     * 获取车辆历史轨迹
     */
    void listHistoricalTrack();


}
