package com.jaagro.tms.api.dto.order;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 毛鸡导入参数
 * @author yj
 * @date 2019/1/8 9:48
 */
@Data
@Accessors(chain = true)
public class ChickenImportRecordDto implements Serializable{
    private static final long serialVersionUID = 4778566167547116437L;
    /**
     * 序列号
     */
    private Integer  serialNumber;
    /**
     * 订单编号
     */
    @NotNull(message = "{orderId.NotNull}")
    @Min(value = 1,message = "{orderId.Min}")
    private Integer orderId;

    /**
     * 客户id
     */
    @NotNull(message = "{customerId.NotNull}")
    @Min(value = 1,message = "{customerId.Min}")
    private Integer customerId;

    /**
     * 客户名称
     */
    @NotBlank(message = "{customerName.NotBlank}")
    private String customerName;

    /**
     * 装货地id
     */
    @NotNull(message = "{loadSiteId.NotNull}")
    @Min(value = 1,message = "{loadSiteId.Min}")
    private Integer loadSiteId;

    /**
     * 装货地名称
     */
    @NotBlank(message = "{loadSiteName.NotBlank}")
    private String loadSiteName;

    /**
     * 装货地所属网点id
     */
    @NotNull(message = "{loadSiteDeptId.NotNull}")
    @Min(value = 1,message = "{loadSiteDeptId.Min}")
    private Integer loadSiteDeptId;

    /**
     * 车辆id
     */
    @NotNull(message = "{truckId.NotNull}")
    @Min(value = 1,message = "{truckId.Min}")
    private Integer truckId;

    /**
     * 车牌号
     */
    @NotBlank(message = "{truckNumber.NotBlank}")
    private String truckNumber;

    /**
     * 车队合同id
     */
    @NotNull(message = "{truckTeamContractId.NotNull}")
    @Min(value = 1,message = "{truckTeamContractId.Min}")
    private Integer truckTeamContractId;

    /**
     * 车型id
     */
    @NotNull(message = "{truckTypeId.NotNull}")
    @Min(value = 1,message = "{truckTypeId.Min}")
    private Integer truckTypeId;

    /**
     * 车型名称
     */
    @NotBlank(message = "{truckTypeName.NotBlank}")
    private String truckTypeName;

    /**
     * 货物数量(单位-筐)
     */
    @NotNull(message = "{goodsQuantity.NotNull}")
    @Min(value = 1,message = "{goodsQuantity.Min}")
    private Integer goodsQuantity;

    /**
     * 货物名称固定为"毛鸡"
     */
    private String goodsName = "毛鸡";
    /**
     * 要求装货时间
     */
    @NotNull(message = "{loadTime.NotNull}")
    private Date loadTime;

    /**
     * 要求送达时间
     */
    @NotNull(message = "{requiredTime.NotNull}")
    private Date requiredTime;

    /**
     * 数据校验是否通过
     */
    private Boolean verifyPass;

    /**
     * 备注信息
     */
    @NotBlank(message = "{notes.NotBlank}")
    private String notes;
}
