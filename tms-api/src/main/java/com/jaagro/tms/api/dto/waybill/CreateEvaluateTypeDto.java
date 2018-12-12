package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class CreateEvaluateTypeDto implements Serializable {
    /**
     * 满意度 （1- 非常差 2- 一般 3-超级赞）
     */
    private Integer satisfactionLever;

    /**
     * 满意度描述
     */
    private String satisfactionLeverDesc;

}
