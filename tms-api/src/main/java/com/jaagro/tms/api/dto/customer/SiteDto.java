package com.jaagro.tms.api.dto.customer;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author gavin
 * 20181219
 */
@Data
@Accessors(chain = true)
public class SiteDto implements Serializable {


    private static final long serialVersionUID = -5826754177339490232L;
    /**
     * 装货地址id
     */
    @NotNull(message = "{loadSiteId.NotNull}")
    private Integer loadSiteId;

    /**
     * 卸货地址id
     */
    @NotNull(message = "{unloadSiteId.NotNull}")
    private Integer unloadSiteId;

}
