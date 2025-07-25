package com.dashboard.app.repo;

import com.dashboard.app.entity.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MasterRepository extends JpaRepository<Master, Long> {
    boolean existsByKey(String key);

    @Query("SELECT m FROM Master m WHERE m.key = :key")
    Master findByProjectKey(@Param("key") String key);

    @Query("SELECT m FROM Master m WHERE m.display = true")
    List<Master> findByDisplay();
}