package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.service.BeanDifferentService;
import com.jaagro.utils.DifferentResult;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author baiyiran
 */
@Service
public class BeanDifferentServiceImpl implements BeanDifferentService {
    @Override
    public Map<String, Object> jointDifferentResult(List<DifferentResult> differentResultList) {
        if (differentResultList != null && differentResultList.size() > 0) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "对比列表为空");
        }
        // 返回的map
        Map<String, Object> resultMap = new HashMap<>();
        // old StringBuffer
        StringBuffer sbOld = new StringBuffer();
        // new StringBuffer
        StringBuffer sbNew = new StringBuffer();

        //循环拼接
        for (DifferentResult differentResult : differentResultList) {
            // 修改过的字段
            List<String> changedAttributes = differentResult.getChangedAttributes();
            if (changedAttributes != null && changedAttributes.size() > 0) {
                // 修改前值的列表
                Map<String, Object> originValue = differentResult.getOriginValue();
                // 修改后值的列表
                Map<String, Object> changedValue = differentResult.getChangedValue();
                if (originValue != null && originValue.size() > 0 && changedValue != null && changedValue.size() > 0) {
                    if (originValue.size() == changedValue.size()) {
                        //拼接旧值
                        for (Map.Entry<String, Object> origin : originValue.entrySet()) {
                            sbOld.append(origin.getKey() + ":" + origin.getValue() + " ");
                        }
                        //拼接新值
                        for (Map.Entry<String, Object> changed : changedValue.entrySet()) {
                            sbNew.append(changed.getKey() + ":" + changed.getValue() + " ");
                        }
                    }
                }
            }
        }
        resultMap.put("originValue", sbOld);
        resultMap.put("changedValue", sbNew);
        return resultMap;
    }
}
