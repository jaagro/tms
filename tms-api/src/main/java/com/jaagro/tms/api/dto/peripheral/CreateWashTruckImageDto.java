package com.jaagro.tms.api.dto.peripheral;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yj
 * @since 2018/12/10
 */
@Data
@Accessors(chain = true)
public class CreateWashTruckImageDto implements Serializable{
    /**
     * 图片路径
     */
    @NotBlank(message = "{imageUrl.NotBlank}")
    private String imageUrl;
    /**
     * 图片类型 1-车前,2-车中,3-车后
     */
    @NotNull(message = "{imageType.NotNull}")
    @Min(value = 1,message = "{imageType.Min}")
    private Integer imageType;
}
