package com.jaagro.tms.api.dto.jindun;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 金盾金盾所有车辆信息
 * @author: @Gao.
 * @create: 2019-04-23 15:47
 **/
@Data
@Accessors(chain = true)
public class JindunTruckDto implements Serializable {

    private String deviceNo;
    private String truckNo;

    public JindunTruckDto() {
    }
    public JindunTruckDto(String deviceNo, String truckNo) {
        this.deviceNo = deviceNo;
        this.truckNo = truckNo;
    }

    private List<JindunTruckDto> truckList = new ArrayList<>();

    public List<JindunTruckDto> JindunTruckDto() {
        truckList.add(new JindunTruckDto("040296013554", "苏NCX003"));
        truckList.add(new JindunTruckDto("040296013548", "苏NC1566"));
        truckList.add(new JindunTruckDto("013318120001", "苏NC9370"));
        truckList.add(new JindunTruckDto("040199566778", "苏NC9638"));
        truckList.add(new JindunTruckDto("040296013549", "苏NCC616"));
        truckList.add(new JindunTruckDto("040296013553", "苏NCH315"));
        truckList.add(new JindunTruckDto("040199566777", "苏NCS619"));
        truckList.add(new JindunTruckDto("040296013547", "苏NCV978"));
        truckList.add(new JindunTruckDto("040296013544", "苏NCW208"));
        truckList.add(new JindunTruckDto("040296013542", "苏NCX758"));
        truckList.add(new JindunTruckDto("040296013545", "苏NCX786"));
        truckList.add(new JindunTruckDto("040296013543", "苏NCY996"));
        return truckList;
    }
}
