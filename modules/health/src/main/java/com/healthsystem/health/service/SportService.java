package com.healthsystem.health.service;

import com.healthsystem.health.domain.entity.Sport;

import java.util.List;

public interface SportService {

    void update(Sport sport);

    Sport get(String userId);

    List<Sport> listSport(List<String> userIds);

    void save(Sport sport);

    void delete(String sportId);
}
