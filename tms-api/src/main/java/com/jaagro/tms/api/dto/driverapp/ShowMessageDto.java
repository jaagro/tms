package com.jaagro.tms.api.dto.driverapp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author @Gao.
 */
@Data
@Accessors(chain = true)
public class ShowMessageDto implements Serializable {
    /**
     * 用于前端封装
     */
    private List<MessageDto> messageDtoList;
}
