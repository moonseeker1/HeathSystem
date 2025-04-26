package com.healthSystem.admin;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户中心
 *
 */

@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.healthSystem"})
@MapperScan("com.healthSystem.**.mapper")
@EnableFeignClients(basePackages = "com.healthSystem.api")
@SpringBootApplication
public class AdminApplication
{
    public static void main(String[] args)
    {

        SpringApplication.run(AdminApplication.class, args);
        System.out.println("///////管理员服务启动成功//////");

    }
}
