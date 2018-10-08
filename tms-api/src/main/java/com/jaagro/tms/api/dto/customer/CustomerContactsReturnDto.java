package com.jaagro.tms.api.dto.customer;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 返回的联系人
 *
 * @author liqiangping
 */
@Data
@Accessors(chain = true)
public class CustomerContactsReturnDto implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 外键关联客户ID(References customer)
     */
    private Integer customerId;

    /**
     * 联系人姓名
     */
    private String contact;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 职位
     */
    private String position;

    /**
     * 状态(0 停用 1 启用)
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 修改人
     */
    private Integer modifyUserId;
}
