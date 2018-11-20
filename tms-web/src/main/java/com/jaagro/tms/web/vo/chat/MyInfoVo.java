package com.jaagro.tms.web.vo.chat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author baiyiran
 * @Date 2018/11/7
 */
@Data
@Accessors(chain = true)
public class MyInfoVo implements Serializable {

    /**
     * 名称
     */
    private Integer customerId;

    /**
     * 名称
     */
    private String Name;

    /**
     * 联系电话
     */
    private String phone;
}
