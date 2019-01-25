package com.jaagro.tms.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.jaagro.tms.api.dto.ocr.MuYuanAppImageDto;
import com.jaagro.tms.api.dto.ocr.MuYuanAppResultDto;
import com.jaagro.tms.api.dto.ocr.WaybillOcrDto;
import com.jaagro.tms.api.service.OcrService;
import com.jaagro.tms.biz.utils.HttpUtils;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tonyZheng
 * @date 2019-01-15 10:10
 */
@Service
public class OcrServiceImpl implements OcrService {

    private static final Logger log = LoggerFactory.getLogger(OcrServiceImpl.class);

    @Override
    public WaybillOcrDto getOcrByMuYuanAppImage(int waybillId, String imageUrl) {
        String jsonResult;
        try {
            jsonResult = ocrUtils(imageUrl);
        }catch (Exception e){
            log.error("O getOcrByMuYuanAppImage Image recognition failed ", e);
            return new WaybillOcrDto();
        }
        System.out.println("源数据：" + jsonResult);
        MuYuanAppImageDto image = JSON.parseObject(jsonResult, MuYuanAppImageDto.class);
        List<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        List<MuYuanAppResultDto> rs = image.getResult();
        for (int i = 0; i < rs.size(); i++) {
            if (rs.get(i).getWords().contains("车号")) {
                out.add(sb.toString());
                sb = new StringBuilder();
            }
            if (i == rs.size() - 1) {
                out.add(sb.toString());
            }
            sb.append(rs.get(i).getWords());
        }
        for (String ss : out) {
            log.info("o getOcrByMuYuanAppImage Before data cleaning: {}", ss);
        }

        //清洗list数据
        Iterator<String> iterator = out.iterator();
        while (iterator.hasNext()){
            if(!(iterator.next().contains("车号") && iterator.next().contains("发料"))){
                iterator.remove();
            }
        }

        WaybillOcrDto waybillOcr = new WaybillOcrDto();
        List<BigDecimal> weightList = new LinkedList<>();

        for (String str : out) {
            String unLoadSite;
            if (str.contains("站")) {
                unLoadSite = str.substring(str.indexOf("罐号") + 4, (str.lastIndexOf("站") + 1));
            } else {
                unLoadSite = str.substring(str.indexOf("罐号") + 4, (str.lastIndexOf("场") + 1));
            }
            String weight = str.substring(str.indexOf("发料"), str.lastIndexOf("总重量"));
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(weight);
            weight = m.replaceAll("").trim();
            //重量如果小于4位数 末位补0
            if(weight.length() < 4){
                weight = weight + "0";
            }
            weightList.add(new BigDecimal(weight));
            waybillOcr.setUnLoadSite(unLoadSite);
        }
        waybillOcr.setWaybillId(waybillId);
        waybillOcr.setGoodsCount(out.size());
        waybillOcr.setGoodsItems(weightList);
        log.info("O getOcrByMuYuanAppImage waybillOrc:{}", waybillOcr);
        return waybillOcr;
    }

    private String ocrUtils(String imageUrl) {
        String host = "https://wordimg.market.alicloudapi.com";
        String path = "/word";
        String method = "POST";
        String appcode = "f2f3d5b3862346aaa87a6b5e1c0ff0f1";
        Map<String, String> headers = new HashMap<>(16);
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>(16);
        Map<String, String> bodys = new HashMap<>(16);
        bodys.put("image", imageUrl);
        HttpResponse response;
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            log.error("R ocrUtils image the image recognition failed: {}", imageUrl);
            return "the image recognition failed:" + imageUrl;
        }
    }
}
