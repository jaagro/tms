package com.jaagro.tms.api.dto.receipt;

import com.jaagro.tms.api.dto.waybill.GetTrackingImagesDto;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 上传回单图片参数
 * @author yj
 * @date 2018/11/9
 */
@Data
@Accessors(chain = true)
public class UploadReceiptImageDto implements Serializable{
    /**
     * 运单id
     */
    @NotNull(message = "{waybillId.NotNull}")
    @Min(value = 1,message = "{waybillId.Min}")
    private Integer waybillId;
    /**
     * 上传图片列表
     */
    @NotEmpty(message = "{uploadImages.NotEmpty}")
    private List<GetTrackingImagesDto> uploadImages;
}
