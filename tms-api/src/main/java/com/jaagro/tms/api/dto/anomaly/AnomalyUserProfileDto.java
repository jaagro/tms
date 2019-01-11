package com.jaagro.tms.api.dto.anomaly;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class AnomalyUserProfileDto implements Serializable {
    /**
     * 客户名称(个体客户时，就是自然人姓名)
     */
    private String customerName;

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 车牌号
     */

    private String truckNumber;
}
