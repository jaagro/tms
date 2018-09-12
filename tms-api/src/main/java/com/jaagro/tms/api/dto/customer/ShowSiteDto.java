package com.jaagro.tms.api.dto.customer;

import com.jaagro.tms.api.dto.waybill.driverapp.GetWaybillTrackingImagesDto;
import com.jaagro.tms.api.dto.waybill.driverapp.ShowGoodsDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ShowSiteDto implements Serializable {

    private Integer id;

    /**
     * 地址类型：1-装货点，2-卸货点
     */
    private Integer siteType;

    /**
     * 装货地名称
     */
    private String siteName;

    /**
     * 联系人姓名
     */
    private String contact;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private String county;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 装货信息 提货要求时间
     */
    private Date loadTime;

    /**
     * 卸货信息 要求送货时间
     */
    private Date requiredTime;

    /**
     * 货物列表
     */
    private List<ShowGoodsDto> goods;

    /**
     * 提货单
     */
    /**
     * 卸货单
     */
    List<GetWaybillTrackingImagesDto> waybillTrackingImagesDtos;
}
