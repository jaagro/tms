package com.jaagro.tms.api.dto.waybill;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class ListEvaluateTypeDto implements Serializable {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 满意度 （1- 非常差 2- 一般 3-超级赞）
     */
    private Integer satisfactionLever;

    /**
     * 满意度描述
     */
    private String satisfactionLeverDesc;

    /**
     * 创建人 id
     */
    private Integer createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人id
     */
    private Integer modifyUserId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     *
     */
    private Boolean enabled;
}
