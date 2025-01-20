package com.healthSystem.gateway.dto;

import lombok.*;

import java.util.List;

/**
 * 权限用户信息封装
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private List<String> permissionList;
}
