package com.dashboard.app.repo;

import com.dashboard.app.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {


    History findTopByMasterIdOrderByUpdatedDateDesc(Long masterId);


}