package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.CatchChickenTime;

import java.util.List;
public interface CatchChickenTimeMapperExt extends CatchChickenTimeMapper{

    List<CatchChickenTime> listCatchChickenTimeByCriteria(CatchChickenTime criteria);

}