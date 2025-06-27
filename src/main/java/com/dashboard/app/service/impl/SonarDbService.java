package com.dashboard.app.service.impl;

import com.dashboard.app.model.ProjectMetrics;
import com.dashboard.app.repo.SonarDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SonarDbService {

    @Autowired
    private SonarDbRepository sonarDbRepository;

    public List<ProjectMetrics> getAllMetricsFromDb() {
        return sonarDbRepository.fetchProjectMetricsFromDb();
    }
}
