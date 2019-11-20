package com.tdpro;

import com.tdpro.common.blocker.UserAdminTokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAutoConfiguration
@SpringBootApplication // 必须标明
@ComponentScan // 开启通用注解扫描
@ServletComponentScan // 扫描使用注解方式的servlet
@EnableAsync
@EnableDiscoveryClient
public class TdproApplication {

    public static void main(String[] args) {
        SpringApplication.run(TdproApplication.class, args);
    }

    @Bean
    public UserAdminTokenFilter userAdminTokenFilter(){
        return new UserAdminTokenFilter();
    }

}
