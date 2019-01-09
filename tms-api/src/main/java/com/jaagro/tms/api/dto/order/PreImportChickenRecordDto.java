package com.jaagro.tms.api.dto.order;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 预导入毛鸡运单参数
 * @author yj
 * @date 2019/1/8 16:20
 */
@Data
@Accessors(chain = true)
public class PreImportChickenRecordDto implements Serializable{
    private static final long serialVersionUID = 2890785276098246601L;
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
     * 上传路径
     */
    @NotBlank(message = "{uploadUrl.NotBlank}")
    private String uploadUrl;
}
