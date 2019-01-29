package com.jaagro.tms.api.dto.truck;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Gavin
 * @Date 20190129
 */
@Data
@Accessors(chain = true)
public class ContractDto implements Serializable {

    private static final long serialVersionUID = -2927945537383624766L;
    /**
     * 合同ID
     */
    private Integer id;
    /**
     * 合同的货物类型
     */
    @NotNull(message = "{goodsType.NotNull}")
    private int goodsType;
    /**
     * 客户Id
     */
    private Integer customerId;
    /**
     * 运单的完成时间
     */
    @NotNull(message = "{waybillDoneDate.NotNull}")
    private Date waybillDoneDate;

    /**
     * 合同类型：1-客户合同；2-运力合同
     */
    @NotNull(message = "{contractType.NotNull}")
    private int contractType;

    /**
     * 关联车队表ID
     */
    private Integer truckTeamId;
    /**
     * 合同状态: 1-正常 2-终止
     */
    private Integer contractStatus;
    /**
     * 合同开始时间
     */
    private Date startDate;

    /**
     * 合同结束时间
     */
    private Date endDate;
}
