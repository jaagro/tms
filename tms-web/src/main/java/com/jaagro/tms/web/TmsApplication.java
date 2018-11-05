package com.jaagro.tms.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author tony
 */
@EnableEurekaClient
@EnableCircuitBreaker
@MapperScan("com.jaagro.tms.biz.mapper")
@EnableFeignClients(basePackages = {"com.jaagro.tms.biz"})
@SpringBootApplication(scanBasePackages = {"com.jaagro.tms"})
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@EnableCaching
public class TmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmsApplication.class, args);
    }
}