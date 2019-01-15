package com.jaagro.tms.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.jaagro.tms.api.dto.ocr.MuYuanAppImageDto;
import com.jaagro.tms.api.dto.ocr.MuYuanAppImageResultDto;
import com.jaagro.tms.api.dto.ocr.WaybillOcrDto;
import com.jaagro.tms.api.service.OcrService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tonyZheng
 * @date 2019-01-15 10:10
 */
@Service
@Log4j
public class OcrServiceImpl implements OcrService {

    @Override
    public WaybillOcrDto getOcrByMuYuanAppImage() {
        String json = "{\"code\":\"1\",\"msg\":\"查询成功\",\"result_num\":28,\"muYuanAppImageResultBo\":[{\"location\":{\"width\":37,\"top\":11,\"left\":26,\"height\":22},\"words\":\"4, ll\"},{\"location\":{\"width\":84,\"top\":13,\"left\":429,\"height\":18},\"words\":\"4814:56\"},{\"location\":{\"width\":122,\"top\":64,\"left\":210,\"height\":34},\"words\":\"签收详情\"},{\"location\":{\"width\":81,\"top\":135,\"left\":91,\"height\":30},\"words\":\"未签收\"},{\"location\":{\"width\":76,\"top\":136,\"left\":367,\"height\":28},\"words\":\"已签收\"},{\"location\":{\"width\":43,\"top\":204,\"left\":477,\"height\":21},\"words\":\"筛选\"},{\"location\":{\"width\":195,\"top\":257,\"left\":173,\"height\":38},\"words\":\"共筛选36条数据\"},{\"location\":{\"width\":444,\"top\":321,\"left\":64,\"height\":29},\"words\":\"车号:苏NCA867司机:贾志明罐号:1万荣四\"},{\"location\":{\"width\":451,\"top\":349,\"left\":61,\"height\":33},\"words\":\"场育肥54单元8#[干粉散装」[13.24发料\"},{\"location\":{\"width\":453,\"top\":381,\"left\":65,\"height\":29},\"words\":\"重量:4000KG总重量:16000KG饲养员:吴锦涛(\"},{\"location\":{\"width\":152,\"top\":411,\"left\":65,\"height\":30},\"words\":\"15503646132)\"},{\"location\":{\"width\":450,\"top\":446,\"left\":68,\"height\":23},\"words\":\"发料编号JWR1S190114009[发料时间12019-01-1414:50\"},{\"location\":{\"width\":444,\"top\":493,\"left\":64,\"height\":30},\"words\":\"车号:苏NCA867司机:贾志明罐号:2万荣四\"},{\"location\":{\"width\":448,\"top\":525,\"left\":64,\"height\":29},\"words\":\"场育肥11单元8#[干粉散装」[13.24发料\"},{\"location\":{\"width\":463,\"top\":555,\"left\":64,\"height\":28},\"words\":\"重量:4000KG总重量:16000KG饲养员:丁赞(15\"},{\"location\":{\"width\":135,\"top\":585,\"left\":57,\"height\":32},\"words\":\"236066272)\"},{\"location\":{\"width\":452,\"top\":622,\"left\":66,\"height\":21},\"words\":\"发料编号WR1S190114009发料时间2019-01-1414:50\"},{\"location\":{\"width\":440,\"top\":669,\"left\":64,\"height\":29},\"words\":\"车号:苏NCA867司机:贾志明罐号:3万荣四\"},{\"location\":{\"width\":458,\"top\":698,\"left\":64,\"height\":29},\"words\":\"场育肥7单元8#[干粉散装][13.24」发料重\"},{\"location\":{\"width\":464,\"top\":729,\"left\":64,\"height\":30},\"words\":\"量:4000KG总重量:16000KG饲养员谢谊浩(19\"},{\"location\":{\"width\":126,\"top\":761,\"left\":65,\"height\":25},\"words\":\"937719211)\"},{\"location\":{\"width\":450,\"top\":795,\"left\":67,\"height\":22},\"words\":\"发料编号WR1S190114009[发料时间2019-01-1414:50\"},{\"location\":{\"width\":444,\"top\":843,\"left\":65,\"height\":28},\"words\":\"车号:苏NCA867司机:贾志明罐号:4万菜四\"},{\"location\":{\"width\":459,\"top\":872,\"left\":64,\"height\":31},\"words\":\"场育肥7单元8#[干粉散装][13.24发料重\"},{\"location\":{\"width\":467,\"top\":900,\"left\":62,\"height\":39},\"words\":\"量:4000KG总重量:16000KG饲养员:谢谊浩(19\"},{\"location\":{\"width\":127,\"top\":934,\"left\":64,\"height\":26},\"words\":\"937719211)\"},{\"location\":{\"width\":444,\"top\":969,\"left\":72,\"height\":22},\"words\":\"发料编号WR1S190114009[发料时间2019-01-1414:50\"},{\"location\":{\"width\":97,\"top\":1021,\"left\":237,\"height\":27},\"words\":\"全屏显示\"}]}";
        MuYuanAppImageDto image = JSON.parseObject(json, MuYuanAppImageDto.class);
        List<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        List<MuYuanAppImageResultDto> results = image.getResult();
        String s = "车号";
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getWords().contains(s)) {
                out.add(sb.toString());
                sb = new StringBuilder();
            }
            if (i == results.size() - 1 && results.get(i).getWords().contains(s)) {
                out.add(sb.toString());
            }
            sb.append(results.get(i).getWords());
        }
        out.remove(0);
        System.out.println(out.size());
        System.out.println(out.get(0));
        Map<String, String> outMap = new HashMap<>(16);

        for(String str : out){
            String unSiteName = str.substring(str.indexOf("罐号") + 4, str.lastIndexOf("场") + 1);
            String weight = str.substring(str.indexOf("发料重量") + 5, str.lastIndexOf("KG"));

            System.out.println(unSiteName);
            System.out.println(weight);
        }
        return null;
    }
}
