package com.jaagro.tms.api.service;

import com.jaagro.utils.DifferentResult;

import java.util.List;
import java.util.Map;

/**
 * @author baiyiran
 */
public interface BeanDifferentService {

    Map<String, Object> jointDifferentResult(List<DifferentResult> differentResultList);

}
