package com.dashboard.app.repo;

import com.dashboard.app.entity.Master;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterRepository extends JpaRepository<Master, Long> {
    boolean existsByKey(String key);
}