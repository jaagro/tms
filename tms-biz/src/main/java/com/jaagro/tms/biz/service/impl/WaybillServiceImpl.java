package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.dto.waybill.CreateWaybillDto;
import com.jaagro.tms.api.dto.waybill.CreateWaybillGoodsDto;
import com.jaagro.tms.api.dto.waybill.CreateWaybillItemsDto;
import com.jaagro.tms.api.dto.waybill.TruckDto;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.mapper.*;
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

import static com.google.common.math.IntMath.mod;

@Service
public class WaybillServiceImpl implements WaybillService {
    private static final Logger log = LoggerFactory.getLogger(WaybillServiceImpl.class);
    @Autowired
    private WaybillMapper waybillMapper;
    @Autowired
    private WaybillTempMapper waybillTempMapper;
    @Autowired
    private WaybillItemsTempMapper waybillItemsTempMapper;
    @Autowired
    private WaybillGoodsTempMapper waybillGoodsTempMapper;
    @Autowired
    private TruckTypeClientService truckTypeClientService;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Override
    public Map<String,Object> createWaybill(CreateWaybillDto waybillDto) {
        Map<String,Object> rtn = new HashMap<>();
        Integer orderId = waybillDto.getOrderId();
        if (StringUtils.isEmpty(orderId)) {
            throw new NullPointerException("订单为空");
        }
        List<TruckDto> truckDtos = waybillDto.getTrucks();
        if(CollectionUtils.isEmpty(truckDtos))
        {
            throw new NullPointerException("车辆为空");
        }
        for(TruckDto truckDto:truckDtos){
            if(truckDto.getNumber()==null || truckDto.getNumber()<=0){
                throw new NullPointerException("车辆数量为空");
            }
        }
        List<CreateWaybillItemsDto>  waybillItemsDtos = waybillDto.getWaybillItems();
        if(CollectionUtils.isEmpty(waybillItemsDtos))
        {
            throw new NullPointerException("送货地址为空");
        }
        for(CreateWaybillItemsDto waybillItemsDto:waybillItemsDtos) {
            List<CreateWaybillGoodsDto> goods =  waybillItemsDto.getGoods();
            if(CollectionUtils.isEmpty(goods))
            {
                throw new NullPointerException("计划配送物品为空");
            }
        }
        List<MiddleObject> middleObjects = new ArrayList<>();
        for(CreateWaybillItemsDto waybillItemsDto:waybillItemsDtos) {
            Integer unloadSiteId = waybillItemsDto.getUnloadSiteId();
            List<CreateWaybillGoodsDto> goods =  waybillItemsDto.getGoods();
                for(CreateWaybillGoodsDto waybillGoodsDto:goods){
                    MiddleObject  middleObject = new MiddleObject();
                    middleObject.setOrderId(orderId);
                    middleObject.setUnloadSiteId(unloadSiteId);
                    middleObject.setOrderGoodsId(waybillGoodsDto.getGoodsId());
                    middleObject.setProportioning(waybillGoodsDto.getProportioning());
                    middleObjects.add(middleObject);
                }
            }

        List<MiddleObject> middleObjects_new = new ArrayList<>();
         //迭代拉直后的物品for (MiddleObject middleObject : middleObjects)
        Iterator<MiddleObject> goodsIterator = middleObjects.iterator();
        while(goodsIterator.hasNext())
         {
             MiddleObject middleObject=goodsIterator.next();
             Integer oldGoodsId = middleObject.getOrderGoodsId();
             //OrderGoods orderGoods = orderGoodsMapper.selectByPrimaryKey(oldGoodsId);
             Integer goodsUnit = 3;//orderGoods.getGoodsUnit();
             Integer proportioning = middleObject.getProportioning();
             Iterator<TruckDto> truckIterator = truckDtos.iterator();
             while(truckIterator.hasNext()){
                 proportioning = middleObject.getProportioning();
                 TruckDto truckDto = truckIterator.next();
                 /*
                 List<ListTruckTypeDto> truckTypeDtos = truckTypeClientService.listTruckTypeReturnDto();
                 truckTypeDtos.stream().forEach(c -> {
                     if (c.getId().equals(truckDto.getTruckId())) {
                         if(goodsUnit==3){
                             truckDto.setCapacity(Integer.valueOf(c.getTruckWeight()));
                         }else{
                             truckDto.setCapacity(Integer.valueOf(c.getTruckAmount()));
                         }
                     }
                 });
                 */
                if(proportioning!= null && proportioning.compareTo(0) >= 0){
                    Integer truckCapacity = truckDto.getCapacity();
                    int modCount = mod(proportioning,truckCapacity);
                    int cars = (proportioning-modCount)/truckCapacity;
                    if(modCount==0 && cars>truckDto.getNumber() && truckDtos.size()==1)
                    {
                        throw new NullPointerException("车子数量不够");
                    }
                    if(cars>truckDto.getNumber())
                    {
                        cars = truckDto.getNumber();
                    }
                    for(int j=0;j<cars;j++){
                        MiddleObject middleObjectNew = new MiddleObject();
                        BeanUtils.copyProperties(middleObject,middleObjectNew);
                        middleObjectNew.setTruckId(truckDto.getTruckId());
                        middleObjectNew.setProportioning(truckCapacity);
                        middleObjectNew.setPlanAmount(truckCapacity);
                        middleObjectNew.setUnPlanAmount(modCount);
                        middleObjects_new.add(middleObjectNew);
                    }
                    if(modCount==0){
                        goodsIterator.remove();
                        if(cars==truckDto.getNumber()){
                            truckIterator.remove();
                        }else {
                            Integer leftCars = truckDto.getNumber() - cars;
                            truckDto.setNumber(leftCars);
                        }
                       break;
                    }else{
                        Integer leftCars = truckDto.getNumber() - cars;
                        truckDto.setNumber(leftCars);
                        if(leftCars==0){truckIterator.remove();}
                        middleObject.setUnPlanAmount(modCount);
                        middleObject.setProportioning(modCount);
                        break;
                    }
                }else {
                    break;
                }
             }
         }
        //保存单一货物配运单
        /*
        try {
            saveWayBillTemp(orderId,middleObjects_new);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存单一货物配运单失败，运单内容={}", middleObjects_new);
            throw new RuntimeException("保存单一货物配运单失败");
        }
*/
        //保存混合货物配运单
        if(!CollectionUtils.isEmpty(middleObjects)){
            try {
                Map<String,List<MiddleObject>>  marginMap=saveWayBillTemp(orderId,middleObjects,truckDtos);
                middleObjects.clear();
                middleObjects.addAll(marginMap.get("margin")) ;
                middleObjects_new.addAll(marginMap.get("assigned"));
            } catch (Exception e) {
                e.printStackTrace();
                log.info("保存混合货物配运单失败，运单内容={}", middleObjects);
                throw new RuntimeException("保存混合货物配运单失败");
            }
        }
        rtn.put("margin",middleObjects);
        rtn.put("assigned",middleObjects_new);
        return rtn;
    }

    /**
     * 保存单一货物临时配送单
     * @param middleObjects
     */
    private void saveWayBillTemp(Integer orderId,List<MiddleObject> middleObjects){
        //查询运货单正式表
        List<Waybill> waybills =  waybillMapper.selectByOrderId(orderId);
        if(CollectionUtils.isEmpty(waybills)) {
            for (MiddleObject middleObj : middleObjects) {
                //没有正式运货单则保存运货单到临时表
                WaybillTemp waybillTemp = waybillTempMapper.getByOrderIdAndTruckId(orderId, middleObj.getTruckId());
                int waybillTempId=0;
                if(waybillTemp==null) {
                    waybillTemp = new WaybillTemp();
                    waybillTemp.setOrderId(middleObj.getOrderId());
                    waybillTemp.setNeedTruckType(middleObj.getTruckId());
                    waybillTemp.setCreateTime(new Date());
                    waybillTempId = waybillTempMapper.insert(waybillTemp);
                }else{
                    waybillTempId = waybillTemp.getId();
                }
                //保存配送地点到临时表
                    WaybillItemsTemp waybillItemsTemp = new WaybillItemsTemp();
                    waybillItemsTemp.setWaybillTempId(waybillTempId);
                    waybillItemsTemp.setUnloadSiteId(middleObj.getUnloadSiteId());
                    int  waybillItemsTempId = waybillItemsTempMapper.insert(waybillItemsTemp);
                    //保存配送货物到临时表
                    WaybillGoodsTemp waybillGoodsTemp = new WaybillGoodsTemp();
                    Integer oldGoodsId = middleObj.getOrderGoodsId();
                    OrderGoods orderGoods = orderGoodsMapper.selectByPrimaryKey(oldGoodsId);
                    Integer goodsUnit = orderGoods.getGoodsUnit();
                    if(goodsUnit==3){
                        waybillGoodsTemp.setGoodsWeight(new BigDecimal(middleObj.getPlanAmount()));
                        waybillGoodsTemp.setGoodsQuantity(0);
                   }else{
                        waybillGoodsTemp.setGoodsQuantity(middleObj.getPlanAmount());
                        waybillGoodsTemp.setGoodsWeight(BigDecimal.ZERO);
                    }
                    waybillGoodsTemp.setWaybillItemsTempId(waybillItemsTempId);
                    waybillGoodsTemp.setOrderGoodsId(oldGoodsId);
                    waybillGoodsTempMapper.insert(waybillGoodsTemp);
            }
        }
    }

    /**
     * 可配量结余时生成临时货运单
     * @param orderId
     * @param middleObjects
     * @param truckDtos
     */
    private Map<String,List<MiddleObject>> saveWayBillTemp(Integer orderId,List<MiddleObject> middleObjects,List<TruckDto> truckDtos){
        Map<String,List<MiddleObject>> rtnMap = new HashMap<>();
        List<MiddleObject> middleObjects_assigned = new ArrayList<>();
        //按配送地排序
        List<MiddleObject> newMiddleObjectsList = middleObjects.stream().sorted((e1,e2)->Integer.compare(e1.getUnloadSiteId(),e2.getUnloadSiteId())).collect(Collectors.toList());
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
                            assignList.clear();
                            break;
                        }
                    }
            }
        }
        rtnMap.put("margin",newMiddleObjectsList);
        rtnMap.put("assigned",middleObjects_assigned);
        return rtnMap;
}
@Data
@Accessors(chain = true)
public class MiddleObject{
    private Integer orderId;
    private Integer truckId;
    private Integer unloadSiteId;
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
        truck1.setTruckId(1);
        truck1.setCapacity(10);
        truck1.setNumber(2);
        TruckDto truck2 = new TruckDto();
        truck2.setTruckId(2);
        truck2.setCapacity(20);
        truck2.setNumber(200);
        TruckDto truck3 = new TruckDto();
        truck3.setTruckId(3);
        truck3.setCapacity(30);
        truck3.setNumber(300);
        truckDtosA.add(truck1);
        truckDtosA.add(truck2);
        truckDtosA.add(truck3);
        TruckDto truck4 = new TruckDto();
        truck4.setTruckId(1);
        truck4.setCapacity(40);
        truck4.setNumber(400);
        truckDtosA.add(truck4);
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, truck1);
        map.put(1, truck2);
        map.put(3, truck3);
        map.put(4, truck4);
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
        List<TruckDto> assignList = new ArrayList<>();
        TruckDto assignDto  = new TruckDto();
        BeanUtils.copyProperties(truck1,assignDto);
        assignList.add(assignDto);

        System.out.println(truckDtosA);
        System.out.println(assignList);
        List<TruckDto> assignedDtos = assignList.stream().filter(c -> c.getTruckId().equals(truck1.getTruckId())).collect(Collectors.toList());
        assignedDtos.get(0).setNumber(200);
        System.out.println(truckDtosA);
        System.out.println(assignList);



    }

}
