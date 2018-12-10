package com.jaagro.tms.api.dto.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 创建洗车记录参数
 * @author yj
 * @since 2018/12/10
 */
@Data
@Accessors(chain = true)
public class CreateWashTruckRecordDto implements Serializable{
    /**
     * 车队id
     */
    @NotNull(message = "{truckTeamId.NotNull}")
    @Min(value = 1,message = "{truckTeamId.Min}")
    private Integer truckTeamId;

    /**
     * 车辆id
     */
    @NotNull(message = "{truckId.NotNull}")
    @Min(value = 1,message = "{truckId.Min}")
    private Integer truckId;

    /**
     * 车牌号码
     */
    @NotBlank(message = "{truckNumber.NotBlank}")
    private String truckNumber;

    /**
     * 司机id
     */
    @NotNull(message = "{driverId.NotNull}")
    @Min(value = 1,message = "{driverId.Min}")
    private Integer driverId;

    /**
     * 司机名称
     */
    @NotBlank(message = "{driverName.NotBlank}")
    private String driverName;

    /**
     * 洗车地址
     */
    @NotBlank(message = "{detailAddress.NotBlank}")
    private String detailAddress;

    /**
     * 备注
     */
    private String notes;

    /**
     * 洗车图片列表
     */
    @Valid
    @NotEmpty(message = "{imageList.NotEmpty}")
    @Size(min = 3,message = "{imageList.SizeMin}")
    private List<CreateWashTruckImageDto> imageList;
}
