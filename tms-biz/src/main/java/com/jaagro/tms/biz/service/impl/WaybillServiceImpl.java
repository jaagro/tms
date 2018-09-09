package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.entity.OrderGoods;
import com.jaagro.tms.biz.entity.OrderItems;
import com.jaagro.tms.biz.entity.Orders;
import com.jaagro.tms.biz.mapper.OrderGoodsMapper;
import com.jaagro.tms.biz.mapper.OrderItemsMapper;
import com.jaagro.tms.biz.mapper.OrdersMapper;
import com.jaagro.tms.biz.mapper.WaybillMapper;
import com.jaagro.tms.biz.service.TruckTypeClientService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WaybillServiceImpl implements WaybillService {
    private static final Logger log = LoggerFactory.getLogger(WaybillServiceImpl.class);
    @Autowired
    private WaybillMapper waybillMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private TruckTypeClientService truckTypeClientService;
    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public List<CreateWaybillDto> createWaybillPlan(CreateWaybillPlanDto waybillDto){
        Integer orderId = waybillDto.getOrderId();
        List<CreateWaybillItemsPlanDto>  waybillItemsDtos = waybillDto.getWaybillItems();
        List<TruckDto> truckDtos = waybillDto.getTrucks();
        List<MiddleObject> middleObjects = new ArrayList<>();
        for(CreateWaybillItemsPlanDto waybillItemsDto:waybillItemsDtos) {
            Integer orderItemId = waybillItemsDto.getOrderItemId();
            List<CreateWaybillGoodsPlanDto> goods =  waybillItemsDto.getGoods();
            for(CreateWaybillGoodsPlanDto waybillGoodsDto:goods){
                MiddleObject  middleObject = new MiddleObject();
                middleObject.setOrderId(orderId);
                middleObject.setOrderItemId(orderItemId);
                middleObject.setOrderGoodsId(waybillGoodsDto.getGoodsId());
                middleObject.setProportioning(waybillGoodsDto.getProportioning());
                middleObjects.add(middleObject);
            }
        }
        List<CreateWaybillDto> waybillDtos = null;
        if(!CollectionUtils.isEmpty(middleObjects)){
            try {
                   waybillDtos=wayBillAlgorithm(middleObjects,truckDtos);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("计算配运单失败，货物内容={}", middleObjects);
                throw new RuntimeException("计算配运单失败");
            }
        }
        return waybillDtos;
    }
    /**
     * 货物配运单核心算法
     * @param middleObjects
     * @param truckDtos
     */
    private List<CreateWaybillDto> wayBillAlgorithm(List<MiddleObject> middleObjects,List<TruckDto> truckDtos){
        List<MiddleObject> middleObjects_assigned = new ArrayList<>();
        List<CreateWaybillDto> waybillDtos = new ArrayList<>();
        //按配送地排序
        List<MiddleObject> newMiddleObjectsList = middleObjects.stream().sorted((e1,e2)->Integer.compare(e1.getOrderItemId(),e2.getOrderItemId())).collect(Collectors.toList());
            Iterator<TruckDto> truckIterator = truckDtos.iterator();
            while(truckIterator.hasNext()) {
                TruckDto truckDto = truckIterator.next();
                Integer capacity = truckDto.getCapacity();
                //使用按照送货地排序后货物分配到车辆生成临时送货单
                List<MiddleObject> assignList = new ArrayList<>();
                for (int k = 0;k<truckDto.getNumber(); k++){
                    Iterator<MiddleObject> goodsIterator = newMiddleObjectsList.iterator();
                    Integer proposioningCount = 0;
                    while (goodsIterator.hasNext()) {
                        MiddleObject middleObject = goodsIterator.next();
                        proposioningCount += middleObject.getProportioning();
                        Integer planAmount  = (middleObject.getProportioning() + capacity - proposioningCount);
                        MiddleObject assignedObj = new MiddleObject();
                        BeanUtils.copyProperties(middleObject,assignedObj);
                        assignList.add(assignedObj);
                        if (proposioningCount > capacity) {
                            List<MiddleObject> assignedDtos = assignList.stream().filter(c -> c.orderGoodsId.equals(middleObject.getOrderGoodsId())).collect(Collectors.toList());
                            assignedDtos.get(0).setTruckId(truckDto.getTruckId());
                            assignedDtos.get(0).setProportioning(planAmount);
                            assignedDtos.get(0).setPlanAmount(planAmount);
                            assignedDtos.get(0).setUnPlanAmount(0);
                            middleObject.setProportioning(proposioningCount - capacity);
                        } else {
                            List<MiddleObject> assignedDtos = assignList.stream().filter(c -> c.orderGoodsId.equals(middleObject.getOrderGoodsId())).collect(Collectors.toList());
                            assignedDtos.get(0).setTruckId(truckDto.getTruckId());
                            assignedDtos.get(0).setPlanAmount(middleObject.getProportioning());
                            assignedDtos.get(0).setUnPlanAmount(0);
                            goodsIterator.remove();
                        }

                        Integer assignTotal = assignList.stream().mapToInt(c -> c.getPlanAmount()).sum();
                        boolean flag = (assignTotal.equals(capacity) || (assignTotal<capacity && newMiddleObjectsList.size()==0));
                        if (flag) {
                            middleObjects_assigned.addAll(assignList);
                            CreateWaybillDto waybillDto = this.makeOneWaybillPlan(assignList);
                            waybillDtos.add(waybillDto);
                            assignList.clear();
                            break;
                        }
                    }
            }
        }
        System.out.println("margin==="+newMiddleObjectsList.toString());
        System.out.println("assigned==="+middleObjects_assigned.toString());
        log.error("货物配运时剩余货物，margin={}", newMiddleObjectsList);
        log.error("货物配运时已经分配的货物，assigned={}", middleObjects_assigned);

        return waybillDtos;
}

    /**
     * 生成一个运单计划
     * @param assignList
     * @return
     */
    private  CreateWaybillDto makeOneWaybillPlan(List<MiddleObject> assignList){
        CreateWaybillDto createwaybillDto = new CreateWaybillDto();
        Orders ordersData = null;
        ListTruckTypeDto truckType = null;
        Integer orderId = 0;
        Integer truckTypeId=0;
        for (MiddleObject obj:assignList) {
            orderId = obj.getOrderId();
            ordersData = ordersMapper.selectByPrimaryKey(orderId);
            if(ordersData == null){
                throw new NullPointerException("订单号为：" + orderId + " 不存在");
            }
            truckTypeId= obj.getTruckId();
            break;
        }
        createwaybillDto.setOrderId(orderId);
        createwaybillDto.setNeedTruckType(truckTypeId);
        createwaybillDto.setLoadSiteId(ordersData.getLoadSiteId());
        createwaybillDto.setTruckTeamContractId(ordersData.getCustomerContractId());

        List<CreateWaybillItemsDto> itemsList = new ArrayList<>();
        for (MiddleObject obj:assignList) {
             OrderGoods orderGoods = orderGoodsMapper.selectByPrimaryKey(obj.getOrderGoodsId());
            Integer goodsUnit = orderGoods.getGoodsUnit();
            if(orderGoods == null){
                throw new NullPointerException("goodsId为：" + obj.getOrderGoodsId()+ " 的货物不存在");
            }
            OrderItems orderItems = orderItemsMapper.selectByPrimaryKey(obj.getOrderItemId());
            CreateWaybillItemsDto createWaybillItemsDto = new CreateWaybillItemsDto();
            createWaybillItemsDto
                    .setUnloadSiteId(orderItems.getUnloadId())
                    .setRequiredTime(orderItems.getUnloadTime());
            List<CreateWaybillGoodsDto> goodsList = new LinkedList<>();
            CreateWaybillGoodsDto createWaybillGoodsDto = new CreateWaybillGoodsDto();
            createWaybillGoodsDto
                    .setGoodsId(orderGoods.getId())
                    .setGoodsName(orderGoods.getGoodsName())
                    .setGoodsQuantity(orderGoods.getGoodsQuantity())
                    .setGoodsUnit(orderGoods.getGoodsUnit())
                    .setJoinDrug(orderGoods.getJoinDrug());
            if(goodsUnit==3){
                createWaybillGoodsDto.setGoodsWeight(new BigDecimal(obj.getPlanAmount()));
            }else{
                createWaybillGoodsDto.setGoodsQuantity(obj.getPlanAmount());
            }
            goodsList.add(createWaybillGoodsDto);
            createWaybillItemsDto.setGoods(goodsList);
            itemsList.add(createWaybillItemsDto);
        }
        createwaybillDto.setWaybillItems(itemsList);
    return createwaybillDto;
 }

@Override
public Map<String,Object> createWaybill(List<CreateWaybillDto> waybillDtos) {
    return null;
}
@Data
@Accessors(chain = true)
public class MiddleObject{
    private Integer orderId;
    private Integer truckId;
    private Integer orderItemId;
    private Integer orderGoodsId;
    /**
     * 可配量
     */
    private Integer proportioning;
    /**
     * 已配量
     */
    private Integer planAmount;
    /**
     * 剩余量
     */
    private Integer unPlanAmount;
}
    public static void main(String[] args) {
        List<TruckDto> truckDtosA = new ArrayList<>();
        TruckDto truck1 = new TruckDto();
        truck1.setTruckId(2);
        truck1.setCapacity(10);
        truck1.setNumber(2);
        TruckDto truck2 = new TruckDto();
        truck2.setTruckId(2);
        truck2.setCapacity(20);
        truck2.setNumber(200);
        TruckDto truck3 = new TruckDto();
        truck3.setTruckId(2);
        truck3.setCapacity(30);
        truck3.setNumber(300);
        truckDtosA.add(truck1);
        truckDtosA.add(truck2);
        truckDtosA.add(truck3);
        TruckDto truck4 = new TruckDto();
        truck4.setTruckId(2);
        truck4.setCapacity(40);
        truck4.setNumber(400);
        truckDtosA.add(truck4);
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, truck1);
        map.put(1, truck2);
        map.put(3, truck3);
        map.put(4, truck4);
        Set<Integer> truckTypeIdSet = truckDtosA.stream().map(c->c.getTruckId()).collect(Collectors.toSet());
        System.out.println(truckTypeIdSet.iterator().next().longValue());
//for(int i=0;i<truck1.getNumber();i++){
//    System.out.println("+++++++++++++++++++");
//    truck1.setNumber(truck1.getNumber()-1);
//}
//        List<TruckDto> dtos = truckDtosA.stream().filter(c->c.getTrunckId().equals(2)).collect(Collectors.toList());
//        dtos.get(0).setNumber(100);
        //System.out.println(dtos);
//        System.out.println(truckDtosA.size());

//        truckDtosA.clear();
//        System.out.println(truckDtosA.size());
//         Iterator<TruckDto> truckIterator = truckDtosA.iterator();
//        while(truckIterator.hasNext()){
//            truckIterator.next();
//            System.out.println("============");
//            truckIterator.remove();
//        }
//        System.out.println(truckDtosA.size());
//        while(truckIterator.hasNext()){
//            truckIterator.next();
//            System.out.println("+++++++++++++");
//            truckIterator.remove();
//        }
        //System.out.println(map);
       // System.out.println(10 <= 10);
//        Integer averageSalary = truckDtosA.stream().mapToInt(c->c.getNumber()).sum();
//        System.out.println(averageSalary);
//        Integer totalCapacity = truckDtosA.stream().mapToInt(c->c.getCapacity()*c.getNumber()).sum();
//        System.out.println(totalCapacity);
//        Set<Integer> officeSet = truckDtosA.stream().map(c->c.getTrunckId()).collect(Collectors.toSet());
//        System.out.println(officeSet);

//        List<TruckDto> names = truckDtosA.stream().filter(c->c.getTrunckId()==1).collect(Collectors.toList());
//        System.out.println(names);
     //   List<TruckDto> truckDtosB = truckDtosA.stream().sorted((e1,e2)->Integer.compare(e1.getTruckId(),e2.getTruckId())).collect(Collectors.toList());
//        List<TruckDto> assignList = new ArrayList<>();
//        TruckDto assignDto  = new TruckDto();
//        BeanUtils.copyProperties(truck1,assignDto);
//        assignList.add(assignDto);
//
//        System.out.println(truckDtosA);
//        System.out.println(assignList);
//        List<TruckDto> assignedDtos = assignList.stream().filter(c -> c.getTruckId().equals(truck1.getTruckId())).collect(Collectors.toList());
//        assignedDtos.get(0).setNumber(200);
//        System.out.println(truckDtosA);
//        System.out.println(assignList);
    }

}
