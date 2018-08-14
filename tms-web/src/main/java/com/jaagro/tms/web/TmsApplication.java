package com.jaagro.tms.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author tony
 */
@EnableEurekaClient
@EnableCircuitBreaker
@ComponentScan("com.jaagro.tms")
@MapperScan("com.jaagro.tms.biz.mapper")
@EnableFeignClients(basePackages = {"com.jaagro.tms.api"})
@SpringBootApplication
public class TmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmsApplication.class, args);
    }
}
