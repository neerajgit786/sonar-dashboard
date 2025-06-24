package com.dashboard.app.repo;

import com.dashboard.app.entity.History;
import com.dashboard.app.entity.Master;
import com.dashboard.app.entity.Metrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HistoryRepository extends JpaRepository<History, Long> {


    History findTopByMasterIdOrderByUpdatedDateDesc(Long masterId);


}