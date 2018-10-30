package com.jaagro.tms.web.vo.chat;

import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author tony
 */
@Data
@Accessors(chain = true)
public class ShowTruckVo implements Serializable {



    /**
     * 车牌号码
     */
    private String truckNumber;


    /**
     * 司机列表
     */
    private List<ShowDriverDto> drivers;
}
