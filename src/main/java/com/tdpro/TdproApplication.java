package com.tdpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAutoConfiguration
@SpringBootApplication // 必须标明
@ComponentScan // 开启通用注解扫描
@EnableAsync
@EnableFeignClients
@EnableDiscoveryClient
public class TdproApplication {

    public static void main(String[] args) {
        SpringApplication.run(TdproApplication.class, args);
    }

}
