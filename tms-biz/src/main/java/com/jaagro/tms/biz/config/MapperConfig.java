package com.jaagro.tms.biz.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author tony
 */
@Configuration
public class MapperConfig{

    @Bean
    public MapperScannerConfigurer setMapperConfig(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.jaagro.tms.biz.mapper");
        mapperScannerConfigurer.setAnnotationClass(Resource.class);
        return mapperScannerConfigurer;
    }
}
