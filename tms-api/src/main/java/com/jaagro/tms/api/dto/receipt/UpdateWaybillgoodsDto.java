package com.jaagro.tms.api.dto.receipt;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 更新运单货物
 * @author yj
 * @date 2018/11/1
 */
@Data
@Accessors(chain = true)
public class UpdateWaybillgoodsDto implements Serializable{
    /**
     * 运单货物id
     */
    @NotNull(message = "{waybillGoodsId.NotNull}")
    @Min(value = 1,message = "{waybillGoodsId.Min}")
    private Integer id;

    /**
     * 运单id
     */
    @NotNull(message = "{waybillId.NotNull}")
    @Min(value = 1,message = "{waybillId.Min}")
    private Integer waybillId;

    /**
     * 运单卸货地id
     */
    @NotNull(message = "{waybillItemId.NotNull}")
    @Min(value = 1,message = "{waybillItemId.Min}")
    private Integer waybillItemId;

    /**
     * 装货数量
     */
    @Min(value = 1,message = "{loadQuantity.Min}")
    private Integer loadQuantity;

    /**
     * 装货重量
     */
    @DecimalMin(value = "0.01",message = "{loadWeight.DecimalMin}")
    private BigDecimal loadWeight;

    /**
     * 卸货数量
     */
    @Min(value = 1,message = "{unloadQuantity.Min}")
    private Integer unloadQuantity;

    /**
     * 卸货重量
     */
    @DecimalMin(value = "0.01",message = "{unloadWeight.DecimalMin}")
    private BigDecimal unloadWeight;

    /**
     * 卸货地id
     */
    @NotNull(message = "{unloadSiteId.NotNull}")
    @Min(value = 1,message = "{unloadSiteId.Min}")
    private Integer unloadSiteId;
}
