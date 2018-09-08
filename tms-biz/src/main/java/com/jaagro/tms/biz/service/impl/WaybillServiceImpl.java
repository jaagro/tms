package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.bo.TruckLoadTempBo;
import com.jaagro.tms.api.constant.GoodsUnit;
import com.jaagro.tms.api.constant.ProductType;
import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.base.ShowTruckTypeDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.TruckTypeClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.security.SignedObject;
import java.util.*;

/**
 * @author tony
 */
@Service
public class WaybillServiceImpl implements WaybillService {

    private static final Logger log = LoggerFactory.getLogger(WaybillService.class);

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private TruckTypeClientService truckTypeClientService;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CreateWaybillDto> createWaybillPlan(CreateWaybillPlanDto waybillPlanDto) {

        //后续挪到controller层判断
        if(StringUtils.isEmpty(waybillPlanDto.getTruckTypes())){
            throw new NullPointerException("需求车型不能为空");
        }
        if(StringUtils.isEmpty(waybillPlanDto.getItems())){
            throw new NullPointerException("订单明细不能为空");
        }
        if(ordersMapper.selectByPrimaryKey(waybillPlanDto.getOrderId()) == null) {
            throw new NullPointerException("订单id不正确");
        }

        //车型信息
        List<TruckLoadTempBo> truckTypeList = new ArrayList<>();
        for(Integer truckTypeId : waybillPlanDto.getTruckTypes()) {
            ListTruckTypeDto truckType = truckTypeClientService.getTruckTypeById(truckTypeId);
            if(truckType == null) {
                throw new NullPointerException("车型id为： " + truckTypeId + "的车不存在");
            }
            TruckLoadTempBo truckLoadTempBo = new TruckLoadTempBo();
            truckLoadTempBo
                    .setId(truckTypeId)
                    .setProductName(truckType.getProductName())
                    .setMaxQuantity(new BigDecimal(truckType.getTruckWeight()))
                    .setLoadQuantity(new BigDecimal(0));
            truckTypeList.add(truckLoadTempBo);
        }
        log.info("待分配车型，共：" + truckTypeList.size() + "台车: " + truckTypeList);

        List<CreateWaybillDto> waybills = new LinkedList<>();

        //循环获取车型
        for(int i = 0; i < truckTypeList.size(); i ++){
            TruckLoadTempBo truckLoadTempBo = truckTypeList.get(i);

            //waybill
            Orders ordersData = ordersMapper.selectByPrimaryKey(waybillPlanDto.getOrderId());
            if(ordersData == null){
                throw new NullPointerException("订单号为：" + waybillPlanDto.getOrderId() + " 不存在");
            }
            CreateWaybillDto createwaybillDto = new CreateWaybillDto();
            createwaybillDto
                    .setNeedTruckType(truckLoadTempBo.getId())
                    .setLoadSiteId(ordersData.getLoadSiteId())
                    .setTruckTeamContractId(ordersData.getCustomerContractId());
            List<CreateWaybillItemsDto> itemsList = new LinkedList<>();

            log.info("waybill：" + createwaybillDto);
            //先把一个车型搞定
            while (truckLoadTempBo.getMaxQuantity().compareTo(truckLoadTempBo.getLoadQuantity()) == 1){
                //所有货物都循环完了还满足不了车，则跳出循环
                boolean stopWhile = false;

                 for(int j =0; j < waybillPlanDto.getItems().size(); j ++){
                    boolean stopItem = false;
                    CreateWaybillItemsPlanDto waybillItemsPlanDto = waybillPlanDto.getItems().get(j);
                    OrderItems orderItems = orderItemsMapper.selectByPrimaryKey(waybillItemsPlanDto.getOrderItemId());
                    //当前车装满后跳出循环
                    if(truckLoadTempBo.getLoadQuantity().compareTo(truckLoadTempBo.getMaxQuantity()) >= 0){
                        break;
                    }
                    for (CreateWaybillGoodsPlanDto goodsPlanDto : waybillItemsPlanDto.getGoods()){
                        //当前车装满后跳出循环
                        if(truckLoadTempBo.getLoadQuantity().compareTo(truckLoadTempBo.getMaxQuantity()) >= 0){
                            break;
                        }
                        OrderGoods orderGoods = orderGoodsMapper.selectByPrimaryKey(goodsPlanDto.getGoodsId());
                        if(orderGoods == null){
                            throw new NullPointerException("goodsId为：" + goodsPlanDto.getGoodsId() + " 的货物不存在");
                        }
                        //waybillGoods
                        CreateWaybillGoodsDto createWaybillGoodsDto = new CreateWaybillGoodsDto();
                        createWaybillGoodsDto
                                .setGoodsName(orderGoods.getGoodsName())
                                .setGoodsQuantity(orderGoods.getGoodsQuantity())
                                .setGoodsUnit(orderGoods.getGoodsUnit())
                                .setJoinDrug(orderGoods.getJoinDrug());
                        //待操作余量 >= 车辆空缺量
                        BigDecimal truckVacancy = truckLoadTempBo.getMaxQuantity().subtract(truckLoadTempBo.getLoadQuantity());
                        if(goodsPlanDto.getSurplus().compareTo(truckVacancy) >= 0){
                            createWaybillGoodsDto
                                    .setGoodsWeight(truckVacancy);
                            //更新待操作余量
                            goodsPlanDto.setSurplus(goodsPlanDto.getSurplus().subtract(truckVacancy));
                            //更新待分配车型的已装量
                            truckLoadTempBo.setLoadQuantity(truckLoadTempBo.getLoadQuantity().add(truckVacancy));
                        } else {
                            //待操作余量 < 车辆装载余量
                            createWaybillGoodsDto
                                    .setGoodsWeight(goodsPlanDto.getSurplus());
                            //更新待分配车型的已装量
                            truckLoadTempBo.setLoadQuantity(truckLoadTempBo.getLoadQuantity().add(goodsPlanDto.getSurplus()));
                            //清空待配在余量
                            goodsPlanDto.setSurplus(BigDecimal.valueOf(0));
                        }
                        //goods列表
                        List<CreateWaybillGoodsDto> goodsList = new LinkedList<>();
                        //waybillItem
                        CreateWaybillItemsDto createWaybillItemsDto = new CreateWaybillItemsDto();
                        createWaybillItemsDto
                                .setUnloadSiteId(orderItems.getUnloadId())
                                .setRequiredTime(orderItems.getUnloadTime());
                        if(createWaybillGoodsDto.getGoodsWeight().compareTo(BigDecimal.valueOf(0)) == 1){
                            goodsList.add(createWaybillGoodsDto);
                        }
                        createWaybillItemsDto
                                .setGoods(goodsList);
                        if(createWaybillItemsDto.getGoods() != null && createWaybillItemsDto.getGoods().size() > 0){
                            itemsList.add(createWaybillItemsDto);
                        }
                        log.debug("goods：" + createWaybillGoodsDto);
                    }
                    //waybillItem循环完了车还没装满
                    if(j + 1 == waybillPlanDto.getItems().size() && truckLoadTempBo.getMaxQuantity().compareTo(truckLoadTempBo.getLoadQuantity()) == 1){
                        stopWhile = true;
                    }
                }
                //所有货物都循环完了还满足不了车，则跳出循环
                if(stopWhile){
                    log.debug(truckLoadTempBo.getId() + " 亏吨了");
                    break;
                }
            }
            //把一个waybill对象包装起来
            createwaybillDto.setWaybillItems(itemsList);
            waybills.add(createwaybillDto);
            log.debug("拆分完成运单：" + createwaybillDto.toString());
        }
        return waybills;
    }
}
