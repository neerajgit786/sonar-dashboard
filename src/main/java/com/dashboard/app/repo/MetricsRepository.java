package com.dashboard.app.repo;

import com.dashboard.app.entity.Metrics;
import com.dashboard.app.model.GameReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MetricsRepository extends JpaRepository<Metrics, Long> {

    @Query(value = """
        SELECT 
            m.name AS gameName,
            m.key AS key,
            m.report_url AS sonarReportUrl,
            TO_CHAR(m.date, 'YYYY-MM-DD HH24:MI:SS') AS date,
            m.gate_status AS qualityGate,
            m.grade AS grade,
            m.RAG_status AS ragStatus,
            CAST(mt.coverage AS VARCHAR) AS codeCoverage,
            CAST(mt.bugs AS VARCHAR) AS bugs,
            CAST(mt.code_smells AS VARCHAR) AS codeSmell,
            CAST(mt.security_hotspots AS VARCHAR) AS security,
            CAST(mt.vulnerabilities AS VARCHAR) AS vulnerabilities,
            CAST(mt.sqale_debt_ratio AS VARCHAR) AS techDebtRatio
        FROM sonar.master m
        LEFT JOIN sonar.metrics mt ON m.id = mt.master_id
        """,
            nativeQuery = true)
    List<Object[]> fetchGameReports();
}