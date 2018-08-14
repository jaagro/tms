package com.jaagro.tms.biz.service;

import com.jaagro.tms.biz.entity.WaybillCodeCounter;
import com.jaagro.tms.biz.mapper.WaybillCodeCounterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 运单编号生成规则
 * @author tony
 */
@Service
public class CodeGenerationService {

    @Autowired
    private WaybillCodeCounterMapper waybillCodeCounterMapper;

    public String generateNewWaybillCode(){
        return null;
    }

    private Integer generateNextCounter(){
        Long maxId = waybillCodeCounterMapper.getMaxCode();
        Integer counter = waybillCodeCounterMapper.selectByPrimaryKey(maxId).getWaybillCounter();
        WaybillCodeCounter waybillCodeCounter = new WaybillCodeCounter();
        waybillCodeCounter.setWaybillCounter(counter + 1).setWaybillDate(new Date());
        waybillCodeCounterMapper.insert(waybillCodeCounter);
        return counter;
    }

    public String generateFullCode(String prefix){
            Integer outCode = generateNextCounter();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String newCode = prefix + sdf.format(new Date())+ String.format("%06d", outCode);
            return newCode;
    }
}
