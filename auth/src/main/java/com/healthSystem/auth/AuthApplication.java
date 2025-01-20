package com.healthSystem.auth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 认证授权中心
 * 
 * @author ruoyi
 */
@EnableFeignClients(basePackages = "com.healthSystem.api")
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.healthSystem","com.healthSystem"})
public class AuthApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(AuthApplication.class, args);
        System.out.println("认证授权中心启动成功" );
    }
}
