package com.jaagro.tms.web.vo.anomaly;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tony
 */
@Data
public class ChangeAnomalyParamVo implements Serializable {
    /**
     * 需要修改的异常id数组
     */
    private int[] ids;

    /**
     * 异常当前状态
     */
    private String nowStatus;

}
