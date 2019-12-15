package com.tdpro;

import com.tdpro.common.blocker.UserAdminTokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.servlet.MultipartConfigElement;

@EnableAutoConfiguration
@SpringBootApplication // 必须标明
@ComponentScan // 开启通用注解扫描
@ServletComponentScan // 扫描使用注解方式的servlet
@EnableAsync
@Configuration
public class TdproApplication {

    public static void main(String[] args) {
        SpringApplication.run(TdproApplication.class, args);
    }

    @Bean
    public UserAdminTokenFilter userAdminTokenFilter(){
        return new UserAdminTokenFilter();
    }

    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize("50MB"); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize("80MB");
        return factory.createMultipartConfig();
    }


}
