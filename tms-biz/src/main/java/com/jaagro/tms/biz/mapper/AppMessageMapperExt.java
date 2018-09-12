package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.AppMessage;

import java.util.List;

/**
 * @author tony
 */
public interface AppMessageMapperExt extends AppMessageMapper {
    /**
     *
     * @param record
     * @return
     *  @author @Gao.
     */
    List<AppMessage> listAppMessageByCondtion(AppMessage record);

}
