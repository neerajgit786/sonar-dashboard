package com.dashboard.app.service.impl;

import com.dashboard.app.model.GameReport;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    public List<GameReport> loadReports(String filePath) {
        List<GameReport> reports = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirst = true;
            while ((line = br.readLine()) != null) {
                if (isFirst) { isFirst = false; continue; } // skip header
                String[] values = line.split(",");

                if (values.length < 11) continue;

                GameReport report = new GameReport();
                report.setGameName(values[0]);
                report.setSonarReportUrl(values[1]);
                report.setDate(values[2]);
                report.setQualityGate(values[3]);
                report.setGrade(values[4]);
                report.setRagStatus(values[5]);
                report.setCodeCoverage(values[6]);
                report.setBugs(values[7]);
                report.setCodeSmell(values[8]);
                report.setSecurity(values[9]);
                report.setVulnerabilities(values[10]);
                report.setTechDebtRatio(values[11]);
                report.setKey(values[12]);

                reports.add(report);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reports;
    }
}

