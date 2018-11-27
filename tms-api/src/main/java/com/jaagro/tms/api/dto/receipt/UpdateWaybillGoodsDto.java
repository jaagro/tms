package com.jaagro.tms.api.dto.receipt;

import lombok.Data;
import lombok.Value;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 更新运单货物
 * @author yj
 * @date 2018/11/1
 */
@Data
@Accessors(chain = true)
public class UpdateWaybillGoodsDto implements Serializable{
    /**
     * 运单货物id
     */
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
    @Min(value = 1,message = "{waybillItemId.Min}")
    private Integer waybillItemId;

    /**
     * 卸货地id 牧原指定卸货地id为零
     */
    @NotNull(message = "{unloadSiteId.NotNull}")
    private Integer unloadSiteId;

    /**
     * 货物名称
     */
    @NotBlank(message = "{goodsName.NotBlank}")
    private String goodsName;

    /**
     * 货物单位：1-羽 2-头 3-吨
     */
    @NotNull(message = "{goodsUnit.NotNull}")
    @Min(value = 1,message = "{goodsUnit.Min}")
    private Integer goodsUnit;
    /**
     * 计划数量
     */
    @Max(value = 9999999,message = "{goodsQuantity.Max}")
    private Integer goodsQuantity;

    /**
     * 计划重量
     */
    @Max(value = 9999999,message = "{goodsWeight.Max}")
    private BigDecimal goodsWeight;

    /**
     * 装货数量
     */
    @Max(value = 9999999,message = "{loadQuantity.Max}")
    private Integer loadQuantity;

    /**
     * 装货重量
     */
    @Max(value = 9999999,message = "{loadWeight.Max}")
    private BigDecimal loadWeight;

    /**
     * 卸货数量
     */
    @Max(value = 9999999,message = "{unloadQuantity.Max}")
    private Integer unloadQuantity;

    /**
     * 卸货重量
     */
    @Max(value = 9999999,message = "{unloadWeight.Max}")
    private BigDecimal unloadWeight;

    /**
     * 是否加药
     */
    private Boolean joinDrug;

    /**
     * 是否有效
     */
    private Boolean enabled;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人id
     */
    private Integer modifyUserId;

    /**
     * 订单货物id
     */
    private Integer orderGoodsId;

    /**
     * 卸货地名称
     */
    private String unloadSiteName;

    /**
     * 要求送达时间
     */
    private Date requiredTime;

    /**
     * 签收状态
     */
    private Boolean signStatus;
}
