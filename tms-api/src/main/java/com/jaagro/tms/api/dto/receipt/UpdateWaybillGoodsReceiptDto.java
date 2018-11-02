package com.jaagro.tms.api.dto.receipt;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 修改运单货物,创建回单记录
 * @author yj
 * @date 2018/11/1
 */
@Data
@Accessors(chain = true)
public class UpdateWaybillGoodsReceiptDto implements Serializable{
    /**
     * 运单货物列表
     */
    @NotEmpty(message = "updateWaybillgoodsDtoList.NotEmpty")
    private List<UpdateWaybillgoodsDto> updateWaybillgoodsDtoList;
    /**
     * 补录原因
     */
    @NotBlank(message = "{reason.NotBlank}")
    private String reason;
}
