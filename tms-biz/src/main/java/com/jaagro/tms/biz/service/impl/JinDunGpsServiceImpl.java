package com.jaagro.tms.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jaagro.tms.api.dto.jindun.JinDunConstant;
import com.jaagro.tms.api.dto.jindun.JindunGspInfoDto;
import com.jaagro.tms.api.dto.jindun.JindunTruckDto;
import com.jaagro.tms.api.dto.waybill.LocationDto;
import com.jaagro.tms.api.service.JinDunGpsService;
import com.jaagro.tms.biz.config.RabbitMqConfig;
import com.jaagro.tms.biz.utils.DateUtil;
import com.jaagro.tms.biz.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description: 金盾服务类
 * @author: @Gao.
 * @create: 2019-04-23 14:26
 **/
@Service
@Slf4j
public class JinDunGpsServiceImpl implements JinDunGpsService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /***
     * 金盾系统登录
     * @return
     */
    @Override
    public String jinDunLogin(boolean expiresType) {
        String jSessionId = redisTemplate.opsForValue().get(JinDunConstant.JSESSION);
        if (StringUtils.isEmpty(jSessionId) || expiresType == true) {
            Map<String, String> param = new HashMap<>(16);
            param.put("account", JinDunConstant.ACCESS_NAME);
            param.put("password", JinDunConstant.ACCESS_PASSWORD);
            String result = HttpClientUtil.doPost(JinDunConstant.LOGIN_URL, param);
            JSONObject jinDunLogin = JSONObject.parseObject(result);
            if (jinDunLogin.getInteger("result") == 0) {
                jSessionId = jinDunLogin.getString("jsession");
                redisTemplate.opsForValue().set(JinDunConstant.JSESSION, jSessionId);
                log.info(jSessionId);
            } else {
                log.info("jing dun login fail.error:", result);
            }
        }
        return jSessionId;
    }

    /**
     * 获取车辆历史轨迹
     */
    @Override
    public void listHistoricalTrack() {
        //登录 获取jsession信息
        String jSession = jinDunLogin(false);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -300);
        String beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Map<String, String> param = new HashMap<>(16);
        param.put("jsession", jSession);
        param.put("begintime", beginTime);
        param.put("endtime", endTime);
        param.put("toMap", String.valueOf(2));
        List<JindunTruckDto> JindunTruckDtos = new JindunTruckDto().JindunTruckDto();
        for (JindunTruckDto jindunTruckDto : JindunTruckDtos) {
            List<LocationDto> locationDtos = new ArrayList<>();
            param.put("devIdno", jindunTruckDto.getDeviceNo());
            String jsonResult = HttpClientUtil.doPost(JinDunConstant.TRACK_URL, param);
            JSONObject jsonObject = JSON.parseObject(jsonResult);
            Integer resultStatus = jsonObject.getInteger("result");
            if (resultStatus == 0) {
                List<JindunGspInfoDto> jindunGspInfoDtos = null;
                JSONArray tracks = jsonObject.getJSONArray("tracks");
                if (tracks != null) {
                    jindunGspInfoDtos = JSON.parseObject(tracks.toJSONString(), new TypeReference<List<JindunGspInfoDto>>() {
                    });
                }
                if (!CollectionUtils.isEmpty(jindunGspInfoDtos)) {
                    for (JindunGspInfoDto jindunGspInfoDto : jindunGspInfoDtos) {
                        if (jindunGspInfoDto.getLat() == null || jindunGspInfoDto == null) {
                            continue;
                        }
                        LocationDto locationDto = new LocationDto();
                        locationDto
                                .setDeviceType(2)
                                .setTruckNumber(jindunTruckDto.getTruckNo())
                                .setDeviceInfo(jindunTruckDto.getDeviceNo())
                                .setSpeed(BigDecimal.valueOf(jindunGspInfoDto.getSp()))
                                .setLatitude(BigDecimal.valueOf(jindunGspInfoDto.getLat()))
                                .setLongitude(BigDecimal.valueOf(jindunGspInfoDto.getLng()))
                                .setLocationTime(DateUtil.stringToDate(jindunGspInfoDto.getGt()));
                        locationDtos.add(locationDto);
                    }
                }
                //保存到消息队列中
                if (!CollectionUtils.isEmpty(locationDtos)) {
                    amqpTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE, "location.send", locationDtos);
                }
            } else {
                log.info("O listHistoricalTrack getGspInfo error", resultStatus);
            }
            //登录jsession失效重新获取
            if (resultStatus == 4) {
                log.info("O listHistoricalTrack jSession expires", resultStatus);
                jinDunLogin(true);
                listHistoricalTrack();
            }
        }
    }
}
