package com.jaagro.tms.api.dto.fee;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询运力费用列表条件
 * @author yj
 * @date 2019/3/4 15:15
 */
@Data
@Accessors(chain = true)
public class ListTruckFeeCriteria implements Serializable{
    private static final long serialVersionUID = 6994609907890686454L;
    /**
     * 当前页
     */
    @NotNull(message = "{pageNum.NotNull}")
    @Min(value = 1,message = "{pageNum.Min}")
    private Integer pageNum;

    /**
     * 每页的数量
     */
    @NotNull(message = "{pageSize.NotNull}")
    @Min(value = 1,message = "{pageSize.Min}")
    private Integer pageSize;

    /**
     * 运单号
     */
    private Integer waybillId;

    /**
     * 车牌号
     */
    private String truckNumber;

    /**
     * 完成时间-起始
     */
    private String completeTimeStart;

    /**
     * 完成时间-结束
     */
    private String completeTimeEnd;

    /**
     * 货物类型
     */
    private Integer goodsType;
}
