package com.healthsystem.health.service;

import com.healthsystem.health.domain.entity.Health;

public interface HealthService {
    public void create(Health health);

    public void update(Health health);

    public Health get(String userId);
}
