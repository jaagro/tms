package com.jaagro.tms.api.dto.base;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 部门对象
 * @author: @Gao.
 * @create: 2019-03-12 14:58
 **/
@Data
@Accessors(chain = true)
public class DepartmentReturnDto implements Serializable {
    /**
     * 部门表主键(网点表)
     */
    private Integer id;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 0-不是，1-是网点
     */
    private Boolean netpoint;

    /**
     * 部门级别
     */
    private Integer level;

    /**
     * 上级部门id
     */
    private Integer parentId;

    /**
     * 上级部门level
     */
    private Integer parentLevel;

    /**
     * 部门主管ID
     */
    private Integer leaderEmployeeId;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区县
     */
    private String county;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人
     */
    private Integer modifyUserId;

    /**
     * 是否可用(0 不可用 1 可用)
     */
    private Boolean enabled;

    /**
     * 排序(越小越靠前)
     */
    private Integer sort;
}
