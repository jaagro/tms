package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.*;
import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.driverapp.*;
import com.jaagro.tms.api.dto.fee.ListTruckFeeCriteria;
import com.jaagro.tms.api.dto.fee.ListTruckFeeDto;
import com.jaagro.tms.api.dto.ocr.WaybillOcrDto;
import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.WaybillRefactorService;
import com.jaagro.tms.biz.config.RabbitMqConfig;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
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
    @Autowired
    private OcrServiceImpl ocrService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private WaybillTruckFeeMapperExt waybillTruckFeeMapper;
    private static int readFromOCRTimes;

    /**
     * 根据状态查询我的运单信息
     *
     * @param dto
     * @return
     * @Author @Gao.
     * @Author @Gao.
     */
    @Override
    public PageInfo listWaybillByStatus(GetWaybillParamDto dto) {

        UserInfo currentUser = currentUserService.getCurrentUser();
        Integer currentUserId = currentUser.getId();
        Waybill waybill = new Waybill();
        waybill.setDriverId(currentUserId);
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        //承运中订单
        if (WaybillConstant.CARRIER.equals(dto.getWaybillStatus())) {
            List<ListWaybillAppDto> waybillDtos = waybillMapper.selectWaybillByCarrierStatus(waybill);
            listWaybill(waybillDtos, currentUserId);
            return new PageInfo<>(waybillDtos);
        }
        //已完成运单
        if (WaybillConstant.ACCOMPLISH.equals(dto.getWaybillStatus())) {
            waybill.setWaybillStatus(WaybillStatus.ACCOMPLISH);
            List<ListWaybillAppDto> waybillDtos = waybillMapper.getWaybillByStatus(waybill);
            listWaybill(waybillDtos, currentUserId);
            return new PageInfo<>(waybillDtos);
        }
        //取消运单
        if (WaybillConstant.CANCEL.equals(dto.getWaybillStatus())) {
            waybill.setWaybillStatus(WaybillStatus.CANCEL);
            List<ListWaybillAppDto> waybillDtos = waybillMapper.getWaybillByStatus(waybill);
            listWaybill(waybillDtos, currentUserId);
            return new PageInfo<>(waybillDtos);
        }
        return null;
    }


    /**
     * 运单列表公共方法
     *
     * @return
     * @author @Gao.
     */
    private void listWaybill(List<ListWaybillAppDto> waybillDtos, Integer currentUserId) {
        if (null != waybillDtos && waybillDtos.size() > 0) {
            for (ListWaybillAppDto waybillDto : waybillDtos) {
                List<ShowGoodsDto> showGoodsDtos = new ArrayList<>();
                List<ShowSiteDto> unloadSiteList = new ArrayList<>();
                //运单号
                waybillDto.setWaybillId(waybillDto.getId());
                //运单状态
                waybillDto.setWaybillStatus(waybillDto.getWaybillStatus());
                //接单时间
                WaybillTracking waybillTrackingCondition = new WaybillTracking();
                waybillTrackingCondition
                        .setWaybillId(waybillDto.getId())
                        .setNewStatus(WaybillStatus.DEPART)
                        .setDriverId(currentUserId);
                WaybillTracking waybillTracking = waybillTrackingMapper.selectSingleTime(waybillTrackingCondition);
                if (null != waybillTracking) {
                    waybillDto.setSingleTime(waybillTracking.getCreateTime());
                }
                //客户信息
                Orders orders = ordersMapper.selectByPrimaryKey(waybillDto.getOrderId());
                if (null != orders) {
                    ShowCustomerDto showCustomerDto = customerClientService.getShowCustomerById(orders.getCustomerId());
                    waybillDto.setCustomer(showCustomerDto);
                }
                //货物信息
                List<GetWaybillItemsAppDto> waybillItems = waybillDto.getWaybillItems();
                if (null != waybillItems && waybillItems.size() > 0) {
                    for (GetWaybillItemsAppDto waybillItem : waybillItems) {
                        //删掉无计划时指定的默认卸货地
                        if (waybillItem.getUnloadSiteId() == 0) {
                            continue;
                        }
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
                waybillDto.setGoods(showGoodsDtos);
                //装货地
                if (null != orders) {
                    ShowSiteDto loadSite = customerClientService.getShowSiteById(orders.getLoadSiteId());
                    waybillDto.setLoadSite(loadSite);
                }
                //卸货地
                waybillDto.setUnloadSite(unloadSiteList);
            }
        }
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
            GetWaybillDetailDto waybillDetailDto = getWaybillDetailById(waybill.getId());

            getWaybills.add(waybillDetailDto);
        }
        return getWaybills;
    }

    /**
     * 根据id获取waybill相关的所有对象
     *
     * @param id
     * @return
     * @Author Gavin
     * @Author Gavin
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
        List<GetWaybillItemDto> getWaybillItemsDtoList = getWaybillItemsAndGoods(waybill.getId());
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
     * 我的运单列表【装卸货端】
     *
     * @param criteriaDto
     * @return
     */
    @Override
    public PageInfo listWebChatWaybillByCriteria(ListWebChatWaybillCriteriaDto criteriaDto) {
        PageHelper.startPage(criteriaDto.getPageNum(), criteriaDto.getPageSize());
        List<ListWaybillDto> listWaybillDtoList = waybillMapper.listWebChatWaybillByCriteria(criteriaDto);
        return new PageInfo<>(listWaybillDtoList);
    }

    @Override
    @RabbitListener(queues = RabbitMqConfig.MUYUAN_OCR_QUEUE)
    @Transactional(rollbackFor = Exception.class)
    public void waybillSupplementByOcr(Map<String, String> map) {
        try {
            int waybillId = Integer.parseInt(map.get("waybillId"));
            String imageUrl = map.get("imageUrl");
            System.out.println(waybillId);
            if (waybillMapper.getWaybillById(waybillId) == null) {
                log.error("R waybillSupplementByOcr waybillId failed:", waybillId);
                return;
            }
            String[] strArray = {imageUrl};
            List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray);
            WaybillOcrDto waybillOcr = ocrService.getOcrByMuYuanAppImage(waybillId, urls.get(0).toString());
            /*add by gavin 访问图片次数*/
            int value = readFromOCRTimes++;
            redisTemplate.opsForValue().getAndSet("readImageFromOCRTimes", String.valueOf(value));
            /**/
            log.info("O waybillSupplementByOcr waybillOcr={}",waybillOcr);
            if (CollectionUtils.isEmpty(waybillOcr.getGoodsItems()) || StringUtils.isEmpty(waybillOcr.getUnLoadSite())) {
                log.error("R waybillSupplementByOcr OCR does not recognize valid data, error data: ", waybillOcr);
                return;
            }
            if (StringUtils.isEmpty(waybillOcr.getUnLoadSite())) {
                log.error("R waybillSupplementByOcr unLoadSite is invalid");
                return;
            }
            //河南牧原id为248，目前图片识别只适用于牧原项目
            //修改waybillItems信息
            String ls = waybillOcr.getUnLoadSite();
            ShowSiteDto showSiteDto = customerClientService.getSiteBySiteName(ls, 248).getData();
            if (showSiteDto == null){
                log.info("R waybillSupplementByOcr showSiteDto is null unloadSite={}",ls);
            }
            List<WaybillItems> waybillItems = waybillItemsMapper.listWaybillItemsByWaybillId(waybillOcr.getWaybillId());
            GetWaybillItemDto cwd = new GetWaybillItemDto();
            WaybillItems wis = new WaybillItems();
            if (!CollectionUtils.isEmpty(waybillItems) && showSiteDto != null) {
                cwd.setId(waybillItems.get(0).getId());
                cwd.setUnloadSiteId(showSiteDto.getId());
                BeanUtils.copyProperties(cwd, wis);
                waybillItemsMapper.updateByPrimaryKeySelective(wis);
            }

            waybillGoodsMapper.deleteByWaybillId(waybillOcr.getWaybillId());
            log.info("O waybillSupplementByOcr update waybillItems, object: {}", wis);
            //根据waybillOcr记录 循环创建waybillGoods;
            List<WaybillGoods> waybillGoodsList = new LinkedList<>();
            for (int i = 0; i < waybillOcr.getGoodsItems().size(); i++) {
                WaybillGoods wg = new WaybillGoods();
                wg.setGoodsName("饲料");
                BigDecimal goodsWeight = waybillOcr.getGoodsItems().get(i).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP);
                wg.setGoodsWeight(goodsWeight);
                wg.setLoadWeight(goodsWeight);
                wg.setUnloadWeight(goodsWeight);
                wg.setWaybillId(waybillOcr.getWaybillId());
                wg.setWaybillItemId(cwd.getId());
                wg.setOrderGoodsId(0);
                wg.setGoodsUnit(GoodsUnit.TON);
                wg.setEnabled(true);
                waybillGoodsList.add(wg);
            }
            //插入数据库
            waybillGoodsMapper.batchInsert(waybillGoodsList);
            log.info("O waybillSupplementByOcr update data success {}", waybillGoodsList);

        } catch (Exception e) {
            log.error("R waybillSupplementByOcr Image recognition failed waybillId: " + map.get("waybillId"), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /**
     * 运力费用列表
     *
     * @param criteria
     * @return
     * @author yj
     */
    @Override
    public PageInfo<ListTruckFeeDto> listTruckFeeByCriteria(ListTruckFeeCriteria criteria) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            PageHelper.startPage(criteria.getPageNum(),criteria.getPageSize());
            if (StringUtils.hasText(criteria.getCompleteTimeStart())){
                criteria.setCompleteTimeStart(criteria.getCompleteTimeStart());
            }
            if (StringUtils.hasText(criteria.getCompleteTimeEnd())){
                criteria.setCompleteTimeEnd(sdf.format(DateUtils.addDays(sdf.parse(criteria.getCompleteTimeEnd()),1)));
            }
            List<ListTruckFeeDto> truckFeeDtoList = waybillTruckFeeMapper.listTruckFeeByCriteria(criteria);
            // 去除重复的卸货地名称
            Set<String> unloadSiteSet = new HashSet<>();
            StringBuilder sb = new StringBuilder();
            for (ListTruckFeeDto dto : truckFeeDtoList){
                String unloadSite = dto.getUnloadSite();
                if (StringUtils.hasText(unloadSite) && unloadSite.contains(",")){
                    String[] unloadSiteArray = unloadSite.split(",");
                    for (int i=0;i<unloadSiteArray.length;i++){
                        unloadSiteSet.add(unloadSiteArray[i]);
                    }
                    unloadSiteSet.forEach(unloadSiteStr->sb.append(unloadSiteStr).append(","));
                    sb.deleteCharAt(sb.lastIndexOf(","));
                    dto.setUnloadSite(sb.toString());
                    sb.delete(0,sb.length());
                    unloadSiteSet.clear();
                }
            }
            return new PageInfo<>(truckFeeDtoList);
        }catch (Exception ex){
            log.error("O listTruckFeeByCriteria error,criteria="+criteria,ex);
            throw new RuntimeException("查询运力费用列表出现异常了!");
        }
    }


    /**
     * 根据waybillId获取Items和goods
     *
     * @param waybillId
     * @return
     * @Author Gavin
     */
    private List<GetWaybillItemDto> getWaybillItemsAndGoods(Integer waybillId) {
        List<GetWaybillItemDto> getWaybillItemsDtoList = new ArrayList<>();
        List<WaybillItems> waybillItemsList = waybillItemsMapper.listWaybillItemsByWaybillId(waybillId);
        for (WaybillItems waybillItems : waybillItemsList) {
            //删掉无计划时指定的默认卸货地
            if (waybillItems.getUnloadSiteId() == 0) {
                continue;
            }
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
        if (!CollectionUtils.isEmpty(getTrackingDtos)) {
            List<Integer> driverIdList = new ArrayList<>();
            List<Integer> employeeIdList = new ArrayList<>();
            for (GetTrackingDto getTrackingDto : getTrackingDtos) {
                if (getTrackingDto.getTrackingType().equals(TrackingType.TRANSPORT)) {
                    driverIdList.add(getTrackingDto.getDriverId());
                } else {
                    employeeIdList.add(getTrackingDto.getReferUserId());
                }
            }
            if (!CollectionUtils.isEmpty(driverIdList)) {
                List<UserInfo> driverList = userClientService.listUserInfo(driverIdList, UserType.DRIVER);
                Map<Integer, UserInfo> driverMap = new HashMap<>();
                driverList.forEach(userInfo -> driverMap.put(userInfo.getId(), userInfo));
                if (!driverMap.isEmpty()) {
                    for (GetTrackingDto getTrackingDto : getTrackingDtos) {
                        if (getTrackingDto.getTrackingType().equals(TrackingType.TRANSPORT)) {
                            getTrackingDto.setUserInfo(driverMap.get(getTrackingDto.getDriverId()));
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(employeeIdList)) {
                List<UserInfo> employeeList = userClientService.listUserInfo(employeeIdList, UserType.EMPLOYEE);
                Map<Integer, UserInfo> employeeMap = new HashMap<>();
                employeeList.forEach(userInfo -> employeeMap.put(userInfo.getId(), userInfo));
                if (!employeeMap.isEmpty()) {
                    for (GetTrackingDto getTrackingDto : getTrackingDtos) {
                        if (!getTrackingDto.getTrackingType().equals(TrackingType.TRANSPORT)) {
                            getTrackingDto.setUserInfo(employeeMap.get(getTrackingDto.getReferUserId()));
                        }
                    }
                }
            }
        }

    }

    private void putTrackingImagesUserInfo(List<GetTrackingImagesDto> getTrackingImagesDtos) {
        //将轨迹图片分为司机上传和调度上传两组
        if (!CollectionUtils.isEmpty(getTrackingImagesDtos)) {
            List<Integer> driverIdList = new ArrayList<>();
            List<Integer> employeeIdList = new ArrayList<>();
            for (GetTrackingImagesDto imagesDto : getTrackingImagesDtos) {
                if (!imagesDto.getImageType().equals(ImagesTypeConstant.RECEIPT_BILL)) {
                    driverIdList.add(imagesDto.getCreateUserId());
                } else {
                    employeeIdList.add(imagesDto.getCreateUserId());
                }
            }
            if (!CollectionUtils.isEmpty(driverIdList)) {
                List<UserInfo> driverList = userClientService.listUserInfo(driverIdList, UserType.DRIVER);
                Map<Integer, UserInfo> driverMap = new HashMap<>();
                driverList.forEach(userInfo -> driverMap.put(userInfo.getId(), userInfo));
                if (!driverMap.isEmpty()) {
                    for (GetTrackingImagesDto imagesDto : getTrackingImagesDtos) {
                        if (!imagesDto.getImageType().equals(ImagesTypeConstant.RECEIPT_BILL)) {
                            imagesDto.setUserInfo(driverMap.get(imagesDto.getCreateUserId()));
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(employeeIdList)) {
                List<UserInfo> employeeList = userClientService.listUserInfo(employeeIdList, UserType.EMPLOYEE);
                Map<Integer, UserInfo> employeeMap = new HashMap<>();
                employeeList.forEach(userInfo -> employeeMap.put(userInfo.getId(), userInfo));
                if (!CollectionUtils.isEmpty(employeeMap)) {
                    for (GetTrackingImagesDto imagesDto : getTrackingImagesDtos) {
                        if (imagesDto.getImageType().equals(ImagesTypeConstant.RECEIPT_BILL)) {
                            imagesDto.setUserInfo(employeeMap.get(imagesDto.getCreateUserId()));
                        }
                    }
                }
            }
        }
    }
}