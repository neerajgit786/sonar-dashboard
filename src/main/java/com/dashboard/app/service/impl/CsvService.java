package com.dashboard.app.service.impl;

import com.dashboard.app.model.GameReport;
import com.dashboard.app.repo.MetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    @Autowired
    MetricsRepository metricsRepository;
    public List<GameReport> loadReports() {
        List<Object[]> rows = metricsRepository.fetchGameReports();
        List<GameReport> reports = new ArrayList<>();

        for (Object[] row : rows) {
            GameReport report = new GameReport(
                    (String) row[0],  // gameName
                    (String) row[1],  // key
                    (String) row[2],  // sonarReportUrl
                    (String) row[3],  // date
                    (String) row[4],  // qualityGate
                    (String) row[5],  // grade
                    (String) row[6],  // ragStatus
                    (String) row[7],  // codeCoverage
                    (String) row[8],  // bugs
                    (String) row[9],  // codeSmell
                    (String) row[10], // security
                    (String) row[11], // vulnerabilities
                    (String) row[12]  // techDebtRatio
            );
            reports.add(report);
        }

        return reports;
    }
}

