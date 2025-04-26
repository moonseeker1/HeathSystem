package com.healthsystem.health.service;

import com.healthsystem.health.domain.entity.Health;

import java.util.List;

public interface HealthService {
    public void create(Health health);

    public void update(Health health);

    public Health get(String userId);

    List<Health> listHealth(List<String> userIds);
}
