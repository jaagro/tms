package com.jaagro.tms.api.dto.jindun;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @description: 金盾历史轨迹
 * @author: @Gao.
 * @create: 2019-04-24 14:53
 **/
@Data
@Accessors(chain = true)
public class JindunGspInfoDto implements Serializable {
    private static final long serialVersionUID = -6366362945847643212L;
    /**
     * 纬度
     */
    private Integer lat;
    /**
     * 经度
     */
    private Integer lng;
    /**
     * 速度
     */
    private Integer sp;
    /**
     * 定位时间
     */
    private String gt;
}
