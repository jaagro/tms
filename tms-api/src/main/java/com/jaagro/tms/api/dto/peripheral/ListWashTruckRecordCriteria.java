package com.jaagro.tms.api.dto.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询洗车记录参数
 * @author yj
 * @since 2018/12/11
 */
@Data
@Accessors(chain = true)
public class ListWashTruckRecordCriteria implements Serializable{
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
    private int pageSize;

    /**
     * 车牌号
     */
    private String truckNumber;

    /**
     * 司机id
     */
    private Integer driverId;
}
