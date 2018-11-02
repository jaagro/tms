package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.TrackingType;
import com.jaagro.tms.api.constant.UserType;
import com.jaagro.tms.api.constant.WaybillConstant;
import com.jaagro.tms.api.constant.WaybillStatus;
import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.driverapp.*;
import com.jaagro.tms.api.dto.receipt.UpdateWaybillGoodsReceiptDto;
import com.jaagro.tms.api.dto.receipt.UpdateWaybillgoodsDto;
import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.WaybillRefactorService;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author tony
 */
@Service
public class WaybillRefactorServiceImpl implements WaybillRefactorService {
    private static final Logger log = LoggerFactory.getLogger(WaybillRefactorServiceImpl.class);

    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private CustomerClientService customerClientService;
    @Autowired
    private WaybillMapperExt waybillMapper;
    @Autowired
    private OrdersMapperExt ordersMapper;
    @Autowired
    private WaybillTrackingMapperExt waybillTrackingMapper;

    @Autowired
    private TruckTypeClientService truckTypeClientService;
    @Autowired
    private WaybillItemsMapperExt waybillItemsMapper;
    @Autowired
    private WaybillGoodsMapperExt waybillGoodsMapper;
    @Autowired
    private WaybillTrackingImagesMapperExt waybillTrackingImagesMapper;
    @Autowired
    private DriverClientService driverClientService;
    @Autowired
    private TruckClientService truckClientService;
    @Autowired
    private UserClientService userClientService;
    @Autowired
    private OssSignUrlClientService ossSignUrlClientService;
    /**
     * 根据状态查询我的运单信息
     * @Author @Gao.
     * @param dto
     * @return
     */
    @Override
    public PageInfo listWaybillByStatus(GetWaybillParamDto dto) {

        UserInfo currentUser = currentUserService.getCurrentUser();
        Integer currentUserId = currentUser.getId();
        Waybill waybill = new Waybill();
        waybill.setDriverId(currentUserId);
        List<ListWaybillAppDto> listWaybillAppDtos;
        //承运中订单
        if (WaybillConstant.CARRIER.equals(dto.getWaybillStatus())) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
            List<GetWaybillAppDto> waybillDtos = waybillMapper.selectWaybillByCarrierStatus(waybill);
            listWaybillAppDtos = listWaybill(waybillDtos, currentUserId);
            return  new PageInfo<>(listWaybillAppDtos);
        }
        //已完成运单
        if (WaybillConstant.ACCOMPLISH.equals(dto.getWaybillStatus())) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
            waybill.setWaybillStatus(WaybillStatus.ACCOMPLISH);
            List<GetWaybillAppDto> waybillDtos = waybillMapper.selectWaybillByStatus(waybill);
            listWaybillAppDtos = listWaybill(waybillDtos, currentUserId);
            return new PageInfo<>(listWaybillAppDtos);
        }
        //取消运单
        if (WaybillConstant.CANCEL.equals(dto.getWaybillStatus())) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
            waybill.setWaybillStatus(WaybillStatus.CANCEL);
            List<GetWaybillAppDto> waybillDtos = waybillMapper.selectWaybillByStatus(waybill);
            listWaybillAppDtos = listWaybill(waybillDtos, currentUserId);
            return new PageInfo<>(listWaybillAppDtos);
        }
        return null;
    }


    /**
     * 运单列表公共方法
     *
     * @return
     * @author @Gao.
     */
    private List<ListWaybillAppDto> listWaybill(List<GetWaybillAppDto> waybillDtos, Integer currentUserId) {
        List<ListWaybillAppDto> listWaybillAppDtos = new ArrayList<>();
        if (null != waybillDtos && waybillDtos.size() > 0) {
            for (GetWaybillAppDto waybillDto : waybillDtos) {
                ListWaybillAppDto listWaybillAppDto = new ListWaybillAppDto();
                List<ShowGoodsDto> showGoodsDtos = new ArrayList<>();
                List<ShowSiteDto> unloadSiteList = new ArrayList<>();
                //运单号
                listWaybillAppDto.setWaybillId(waybillDto.getId());
                //运单状态
                listWaybillAppDto.setWaybillStatus(waybillDto.getWaybillStatus());
                //接单时间
                WaybillTracking waybillTrackingCondition = new WaybillTracking();
                waybillTrackingCondition
                        .setWaybillId(waybillDto.getId())
                        .setNewStatus(WaybillStatus.DEPART)
                        .setDriverId(currentUserId);
                WaybillTracking waybillTracking = waybillTrackingMapper.selectSingleTime(waybillTrackingCondition);
                if (null != waybillTracking) {
                    listWaybillAppDto.setSingleTime(waybillTracking.getCreateTime());
                }
                //客户信息
                Orders orders = ordersMapper.selectByPrimaryKey(waybillDto.getOrderId());
                if (null != orders) {
                    ShowCustomerDto showCustomerDto = customerClientService.getShowCustomerById(orders.getCustomerId());
                    listWaybillAppDto.setCustomer(showCustomerDto);
                }
                //货物信息
                List<GetWaybillItemsAppDto> waybillItems = waybillDto.getWaybillItems();
                if (null != waybillItems && waybillItems.size() > 0) {
                    for (GetWaybillItemsAppDto waybillItem : waybillItems) {
                        if (null != waybillItem) {
                            ShowSiteDto unloadSite = customerClientService.getShowSiteById(waybillItem.getUnloadSiteId());
                            unloadSiteList.add(unloadSite);
                        }
                        List<ShowGoodsDto> goods = waybillItem.getGoods();
                        for (ShowGoodsDto good : goods) {
                            showGoodsDtos.add(good);
                        }
                    }
                }
                listWaybillAppDto.setGoods(showGoodsDtos);
                //装货地
                if (null != orders) {
                    ShowSiteDto loadSite = customerClientService.getShowSiteById(orders.getLoadSiteId());
                    listWaybillAppDto.setLoadSite(loadSite);
                }
                //卸货地
                listWaybillAppDto.setUnloadSite(unloadSiteList);
                listWaybillAppDtos.add(listWaybillAppDto);
            }
        }
        return listWaybillAppDtos;
    }

    /**
     * 根据订单号获取运单列表
     *
     * @param orderId
     * @return
     * @Author Gavin
     */
    @Override
    public List<GetWaybillDetailDto> listWaybillDetailByOrderId(Integer orderId) {
        List<Waybill> waybillList = waybillMapper.listWaybillByOrderId(orderId);
        if (waybillList == null) {
            throw new NullPointerException("当前订单无有效运单");
        }
        List<GetWaybillDetailDto> getWaybills = new ArrayList<>();
        for (Waybill waybill : waybillList) {
            GetWaybillDetailDto waybillDetailDto =getWaybillDetailById(waybill.getId());

            getWaybills.add(waybillDetailDto);
        }
        return getWaybills;
    }

    /**
     * 根据id获取waybill相关的所有对象
     * @Author Gavin
     * @param id
     * @return
     */

    @Override
    public GetWaybillDetailDto getWaybillDetailById(Integer id) {
        //拿到waybill对象
        Waybill waybill = waybillMapper.selectByPrimaryKey(id);
        if (null == waybill) {
            throw new NullPointerException(id + ": 运单不存在");
        }
        //拿到装货地对象
        ShowSiteDto loadSiteDto = customerClientService.getShowSiteById(waybill.getLoadSiteId());

        //拿到车型对象
        ListTruckTypeDto truckTypeDto = null;
        if (!StringUtils.isEmpty(waybill.getNeedTruckType())) {
            truckTypeDto = truckTypeClientService.getTruckTypeById(waybill.getNeedTruckType());
        }

        ShowDriverDto showDriverDto = null;
        if (!StringUtils.isEmpty(waybill.getDriverId())) {
            showDriverDto = driverClientService.getDriverReturnObject(waybill.getDriverId());
        }
        //运单指派给车辆对象
        ShowTruckDto truckDto = null;
        if (!StringUtils.isEmpty(waybill.getTruckId())) {
            truckDto = truckClientService.getTruckByIdReturnObject(waybill.getTruckId());
        }
        //获取waybillItem以及对应的货物列表
        List<GetWaybillItemDto> getWaybillItemsDtoList=getWaybillItemsAndGoods(waybill.getId());
        //根据waybillId获取WaybillTracking
        List<GetTrackingDto> getTrackingDtos = new ArrayList<>();
        List<ShowTrackingDto> showTrackingDtos = waybillTrackingMapper.getWaybillTrackingByWaybillId(waybill.getId());
        for (ShowTrackingDto showTrackingDto : showTrackingDtos) {
            GetTrackingDto getTrackingDto = new GetTrackingDto();
            BeanUtils.copyProperties(showTrackingDto, getTrackingDto);
            getTrackingDtos.add(getTrackingDto);
        }
        //设置轨迹上传人信息
        putTrackingUserInfo(getTrackingDtos);
        //根据waybillId获取所有的轨迹上传上来的图片WaybillTrackingImages
        WaybillTrackingImages record = new WaybillTrackingImages();
        record.setWaybillId(waybill.getId());
        List<GetWaybillTrackingImagesDto> getWaybillTrackingImagesDtos = waybillTrackingImagesMapper.listWaybillTrackingImage(record);

        List<GetTrackingImagesDto> getTrackingImagesDtos = new ArrayList<>();
        for (GetWaybillTrackingImagesDto getWaybillTrackingImagesDto : getWaybillTrackingImagesDtos) {
            GetTrackingImagesDto getTrackingImagesDto = new GetTrackingImagesDto();
            BeanUtils.copyProperties(getWaybillTrackingImagesDto, getTrackingImagesDto);
            String[] strArray = {getTrackingImagesDto.getImageUrl()};
            List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray);
            getTrackingImagesDto.setImageUrl(urls.get(0).toString());
            getTrackingImagesDtos.add(getTrackingImagesDto);
        }
        //设置轨迹图片上传人信息
        putTrackingImagesUserInfo(getTrackingImagesDtos);
        //把图片塞进对应的轨迹中
        for (GetTrackingDto getTrackingDto : getTrackingDtos) {
            List<GetTrackingImagesDto> imageList = getTrackingImagesDtos.stream().filter(c -> c.getWaybillTrackingId().equals(getTrackingDto.getId())).collect(Collectors.toList());
            getTrackingDto.setImageList(imageList);
        }
        Orders ordersData = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
        GetWaybillDetailDto getWaybillDto = new GetWaybillDetailDto();
        getWaybillDto.setTracking(getTrackingDtos);
        BeanUtils.copyProperties(waybill, getWaybillDto);
        getWaybillDto
                .setLoadSiteDto(loadSiteDto)
                .setNeedTruckTypeDto(truckTypeDto)
                .setAssginedTruckDto(truckDto)
                .setDriverDto(showDriverDto)
                .setWaybillItems(getWaybillItemsDtoList)
                .setGoodType(ordersData.getGoodsType());
        return getWaybillDto;
    }


    /**
     *
     * 根据waybillId获取Items和goods
     * @Author Gavin
     * @param waybillId
     * @return
     */
    public List<GetWaybillItemDto> getWaybillItemsAndGoods(Integer waybillId){
        List<GetWaybillItemDto> getWaybillItemsDtoList = new ArrayList<>();
        List<WaybillItems> waybillItemsList = waybillItemsMapper.listWaybillItemsByWaybillId(waybillId);
        for (WaybillItems waybillItems : waybillItemsList) {
            GetWaybillItemDto getWaybillItemsDto = new GetWaybillItemDto();
            BeanUtils.copyProperties(waybillItems, getWaybillItemsDto);
            List<GetWaybillGoodsDto> getWaybillGoodsDtoList = new LinkedList<>();
            //获取每个卸货地的货物
            List<WaybillGoods> waybillGoodsList = waybillGoodsMapper.listWaybillGoodsByItemId(waybillItems.getId());
            for (WaybillGoods wg : waybillGoodsList) {
                GetWaybillGoodsDto getWaybillGoodsDto = new GetWaybillGoodsDto();
                BeanUtils.copyProperties(wg, getWaybillGoodsDto);
                getWaybillGoodsDtoList.add(getWaybillGoodsDto);
            }
            //拿到卸货信息
            ShowSiteDto unloadSite = customerClientService.getShowSiteById(waybillItems.getUnloadSiteId());
            getWaybillItemsDto.setShowSiteDto(unloadSite);
            getWaybillItemsDto.setGoods(getWaybillGoodsDtoList);
            getWaybillItemsDtoList.add(getWaybillItemsDto);
        }
        return getWaybillItemsDtoList;
    }

    private void putTrackingUserInfo(List<GetTrackingDto> getTrackingDtos) {
        //将轨迹分为司机上传和调度上传两组
        if (!CollectionUtils.isEmpty(getTrackingDtos)){
            List<Integer> driverIdList = new ArrayList<Integer>();
            List<Integer> employeeIdList = new ArrayList<Integer>();
            for (GetTrackingDto getTrackingDto : getTrackingDtos){
                if (getTrackingDto.getTrackingType() == 1){
                    driverIdList.add(getTrackingDto.getDriverId());
                }else{
                    employeeIdList.add(getTrackingDto.getReferUserId());
                }
            }
            if (!CollectionUtils.isEmpty(driverIdList)){
                List<UserInfo> driverList = userClientService.listUserInfo(driverIdList, UserType.DRIVER);
                if (!CollectionUtils.isEmpty(driverList)){
                    for (GetTrackingDto getTrackingDto : getTrackingDtos){
                        for (UserInfo userInfo : driverList){
                            if (getTrackingDto.getTrackingType() == 1 && userInfo.getId().equals(getTrackingDto.getDriverId())){
                                getTrackingDto.setUserInfo(userInfo);
                            }
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(employeeIdList)){
                List<UserInfo> employeeList = userClientService.listUserInfo(employeeIdList, UserType.EMPLOYEE);
                if (!CollectionUtils.isEmpty(employeeList)){
                    for (GetTrackingDto getTrackingDto : getTrackingDtos){
                        for (UserInfo userInfo : employeeList){
                            if (getTrackingDto.getTrackingType() == 2 && userInfo.getId().equals(getTrackingDto.getReferUserId())){
                                getTrackingDto.setUserInfo(userInfo);
                            }
                        }
                    }
                }
            }
        }

    }

    private void putTrackingImagesUserInfo(List<GetTrackingImagesDto> getTrackingImagesDtos) {
        //将轨迹图片分为司机上传和调度上传两组
        if (!CollectionUtils.isEmpty(getTrackingImagesDtos)){
            List<Integer> driverIdList = new ArrayList<Integer>();
            List<Integer> employeeIdList = new ArrayList<Integer>();
            for (GetTrackingImagesDto imagesDto : getTrackingImagesDtos){
                if (imagesDto.getImageType() != 3){
                    driverIdList.add(imagesDto.getCreateUserId());
                }else{
                    employeeIdList.add(imagesDto.getCreateUserId());
                }
            }
            if (!CollectionUtils.isEmpty(driverIdList)){
                List<UserInfo> driverList = userClientService.listUserInfo(driverIdList, UserType.DRIVER);
                if (!CollectionUtils.isEmpty(driverList)){
                    for (GetTrackingImagesDto imagesDto : getTrackingImagesDtos){
                        for (UserInfo userInfo : driverList){
                            if (imagesDto.getImageType() != 3 && userInfo.getId().equals(imagesDto.getCreateUserId())){
                                imagesDto.setUserInfo(userInfo);
                            }
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(employeeIdList)){
                List<UserInfo> employeeList = userClientService.listUserInfo(employeeIdList, UserType.EMPLOYEE);
                if (!CollectionUtils.isEmpty(employeeList)){
                    for (GetTrackingImagesDto imagesDto : getTrackingImagesDtos){
                        for (UserInfo userInfo : employeeList){
                            if (imagesDto.getImageType() == 3 && userInfo.getId().equals(imagesDto.getCreateUserId())){
                                imagesDto.setUserInfo(userInfo);
                            }
                        }
                    }
                }
            }
        }
    }
}