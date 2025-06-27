package com.dashboard.app.repo;

import com.dashboard.app.model.ProjectMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

@Repository
public class SonarDbRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ProjectMetrics> fetchProjectMetricsFromDb() {
        String sql = """
            SELECT
                p.uuid AS project_key,
                p.name AS project_name,
                qg.status AS gate_status,
                m.metric_key,
                mv.value
            FROM
                projects p
            LEFT JOIN quality_gates qg ON p.quality_gate_id = qg.id
            JOIN measures mv ON p.id = mv.component_id
            JOIN metrics m ON mv.metric_id = m.id
            WHERE
                p.qualifier = 'TRK'
                AND m.metric_key IN ('coverage', 'bugs', 'code_smells', 'vulnerabilities', 'security_hotspots')
                AND mv.analysis_uuid = (
                    SELECT MAX(analysis_uuid)
                    FROM measures sub_mv
                    WHERE sub_mv.component_id = p.id
                      AND sub_mv.metric_id = mv.metric_id
                )
            """;

        Map<String, ProjectMetrics> resultMap = new LinkedHashMap<>();

        jdbcTemplate.query(sql, rs -> {
            String key = rs.getString("project_key");
            ProjectMetrics metrics = resultMap.computeIfAbsent(key, k -> {
                ProjectMetrics pm = new ProjectMetrics();
                pm.setProjectKey(k);
                try {
                    pm.setProjectName(rs.getString("project_name"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    pm.setGateStatus(rs.getString("gate_status"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return pm;
            });

            String metric = rs.getString("metric_key");
            String value = rs.getString("value");

            switch (metric) {
                case "coverage" -> metrics.setCoverage(Double.parseDouble(value));
                case "bugs" -> metrics.setBugs(Integer.parseInt(value));
                case "code_smells" -> metrics.setCodeSmells(Integer.parseInt(value));
                case "vulnerabilities" -> metrics.setVulnerabilities(Integer.parseInt(value));
                case "security_hotspots" -> metrics.setSecurityHotspots(Integer.parseInt(value));
            }
        });

        return new ArrayList<>(resultMap.values());
    }
}
