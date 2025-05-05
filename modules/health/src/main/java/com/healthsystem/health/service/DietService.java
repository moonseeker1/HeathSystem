package com.healthsystem.health.service;

import com.healthsystem.health.domain.entity.Diet;
import com.healthsystem.health.domain.entity.Health;

import java.util.List;

public interface DietService {
    void save(Diet diet);

    void update(Diet diet);

    Diet get(String userId);

    List<Diet> listDiet(List<String> userIds);

    void delete(String dietId);
}
