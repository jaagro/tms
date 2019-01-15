package com.jaagro.tms.api.dto.order;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新毛鸡导入记录参数
 *
 * @author yj
 * @date 2019/1/15 13:46
 */
@Data
@Accessors(chain = true)
public class UpdateChickenImportRecordDto implements Serializable {
    private static final long serialVersionUID = 5145301853613050107L;
    /**
     * 序列号
     */
    @NotNull(message = "{serialNumber.NotNull}")
    @Min(value = 1, message = "{serialNumber.Min}")
    private Integer serialNumber;

    /**
     * 订单编号
     */
    @NotNull(message = "{orderId.NotNull}")
    @Min(value = 1, message = "{orderId.Min}")
    private Integer orderId;

    /**
     * 车牌号
     */
    @NotBlank(message = "{truckNumber.NotBlank}")
    private String truckNumber;
}
