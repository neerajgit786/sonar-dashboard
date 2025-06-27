package com.dashboard.app.repo;

import com.dashboard.app.entity.Metrics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricsRepository extends JpaRepository<Metrics, Long> {}