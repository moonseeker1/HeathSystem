package com.healthSystem.user.domain.vo;

import lombok.Data;

@Data
public class SystemUserHealth {

    String userId;
    String userName;
    Integer age;
    Integer sex;
    Double height;
    Double weight;
    String bloodPressure;
    String bloodSugar;
}
