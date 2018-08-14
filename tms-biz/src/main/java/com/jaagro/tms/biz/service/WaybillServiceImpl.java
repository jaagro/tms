package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.CreateWaybillByExcelDto;
import com.jaagro.tms.api.dto.LoadExcelDto;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.api.dto.NewWaybillDto;
import com.jaagro.tms.biz.entity.Waybill;
import com.jaagro.tms.biz.entity.WaybillProduct;
import com.jaagro.tms.biz.entity.WaybillProductItem;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.api.exception.LoadExcelException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.ServiceResult;

import java.util.Date;
import java.util.Map;

/**
 * @author tony
 */
@Service
public class WaybillServiceImpl implements WaybillService {

    @Autowired
    private OldUserService oldUserService;
    @Autowired
    private PickUpAddressMapper pickUpAddressMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private WaybillMapper waybillMapper;
    @Autowired
    private CodeGenerationService codeGenerationService;
    @Autowired
    private ShippingAddressMapper shippingAddressMapper;
    @Autowired
    private WaybillProductMapper waybillProductMapper;
    @Autowired
    private WaybillProductItemMapper waybillProductItemMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createWaybillByExcel(CreateWaybillByExcelDto waybills) throws Exception {

        if(!StringUtils.isEmpty(waybills)) {

            //创建waybill对象
            NewWaybillDto waybillDto = new NewWaybillDto();
            WaybillProduct waybillProduct = new WaybillProduct();
            WaybillProductItem waybillProductItem;
            WaybillProduct wp = new WaybillProduct();
            for (LoadExcelDto excelValue : waybills.getWaybills()) {
                if(!StringUtils.isEmpty(waybillDto.getCode()) && waybillDto.getCode().equals(excelValue.getCode())){

                    waybillProductItem = new WaybillProductItem();
                    //插入waybillProductItem
                    BeanUtils.copyProperties(excelValue, waybillProductItem);
                    waybillProductItem.setWaybillProductId(waybillProduct.getWaybillProductId());
                    waybillProductItemMapper.insertSelective(waybillProductItem);
                    //修改waybillProduct的合计字段
                    wp
                            .setQuantity(wp.getQuantity() + waybillProductItem.getQuantity())
                            .setWeight(wp.getWeight().add(waybillProductItem.getWeight()))
                            .setProductName(wp.getProductName() + "/" + waybillProductItem.getProductName());
                    System.out.println(wp);
                    waybillProductMapper.updateByPrimaryKeySelective(wp);
                    waybillProductMapper.updateByPrimaryKeySelective(wp);
                    System.out.println(waybillProductItem);
                    continue;
                }else{
                    waybillProduct = new WaybillProduct();
                    waybillProductItem = new WaybillProductItem();
                    wp = new WaybillProduct();
                }
                BeanUtils.copyProperties(excelValue, waybillDto);
                try {
                    waybillDto
                            .setCreatedDate(new Date())
                            .setBranchId(oldUserService.getCurrentBranch().getBranchId())
                            .setUserId(oldUserService.getCurrentUser().getUserId())
                            .setCreatedUserId(oldUserService.getCurrentUser().getUserId())
                            .setWaybillCode(codeGenerationService.generateFullCode("Y"))
                            .setCustomerName(customerMapper.getCustomerById(pickUpAddressMapper.getPickUpAddressByName(excelValue.getCustomer()).getCustomerId()).getName())
                            .setCustomerId(pickUpAddressMapper.getPickUpAddressByName(excelValue.getCustomer()).getCustomerId())
                            .setPickUpAddress(excelValue.getCustomer())
                            .setPickUpAddressId(pickUpAddressMapper.getPickUpAddressByName(excelValue.getCustomer()).getPickUpAddressId())
                            .setProductTypeId(productTypeMapper.getProductByName(excelValue.getProductType()).getProductTypeId())
                            .setContactPerson(pickUpAddressMapper.getPickUpAddressByName(excelValue.getCustomer()).getContactPerson())
                            .setContactNumber(pickUpAddressMapper.getPickUpAddressByName(excelValue.getCustomer()).getContactNumber())
                            .setPaymentTypeId(2L)
                            .setIsActive(true)
                            .setWaybillStatusId(1L)
                            .setTransportationTypeId(1L)
                            .setWaybillValidatingStatusId(1L);
                } catch (NullPointerException e) {
                    throw new LoadExcelException("表格导入数据不合法，请检查后再导入。原因可能是表格中一些重要信息在系统中不存在");
                }
                //waybill_id在这里
                Waybill createWaybill = new Waybill();
                BeanUtils.copyProperties(waybillDto, createWaybill);
                waybillMapper.insertSelective(createWaybill);
                System.out.println(createWaybill);

                //插入waybillProduct
                BeanUtils.copyProperties(excelValue, waybillProduct);
                try {
                    waybillProduct
                            .setWaybillId(createWaybill.getWaybillId())
                            .setContactPerson(shippingAddressMapper.getShippingAddressByName(waybillProduct.getShippingAddress()).getContactPerson())
                            .setContactNumber(shippingAddressMapper.getShippingAddressByName(waybillProduct.getShippingAddress()).getContactNumber())
                            .setDetailedAddress(shippingAddressMapper.getShippingAddressByName(waybillProduct.getShippingAddress()).getAddress())
                            .setPricingMethodId(1L)
                            .setIsActive(true)
                            .setReceiptStatusId(1L)
                            .setShippingAddressId(shippingAddressMapper.getShippingAddressByName(excelValue.getShippingAddress()).getShippingAddressId());
                } catch (NullPointerException e) {
                    throw new LoadExcelException("表格导入数据不合法，请检查后再导入。原因可能是表格中一些重要信息在系统中不存在");
                }
                waybillProductMapper.insertSelective(waybillProduct);
                System.out.println(waybillProduct);

                //插入waybillProductItem
                BeanUtils.copyProperties(excelValue, waybillProductItem);
                waybillProductItem.setWaybillProductId(waybillProduct.getWaybillProductId());
                waybillProductItemMapper.insertSelective(waybillProductItem);
                System.out.println(waybillProductItem);

                //获取waybillProduct的合计信息
                wp
                        .setWaybillProductId(waybillProduct.getWaybillProductId())
                        .setQuantity(waybillProductItem.getQuantity())
                        .setWeight(waybillProductItem.getWeight())
                        .setProductName(waybillProductItem.getProductName());
            }
        }
        return ServiceResult.toResult("导入成功");
    }
}
