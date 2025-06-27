package com.dashboard.app.util;

import com.dashboard.app.model.Result;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class GradeCalculator {

    public static Result calculateGradeAndRag(double coverage, int bugs, int security, int vulnerabilities, Double maintainabilityIssues) {
        Double totalIssues = bugs + security + vulnerabilities + maintainabilityIssues;

        if (coverage >= 80 && totalIssues == 0) {
            return new Result("A", "Green");
        } else if (coverage >= 60 && coverage < 80 && totalIssues == 0) {
            return new Result("B", "Amber");
        } else if (coverage >= 60 && coverage < 80 && totalIssues <= 10) {
            return new Result("C", "Amber");
        } else if (coverage >= 60 && coverage < 80 && totalIssues > 10) {
            return new Result("D", "Red");
        } else {
            return new Result("E", "Red");
        }
    }


//    public static String calculateGrade(int vulnerabilities, double techDebtRatio, int bugs) {
//        char secGrade = getSecurityGrade(vulnerabilities);
//        char maintainGrade = getMaintainabilityGrade(techDebtRatio);
//        char relGrade = getReliabilityGrade(bugs);
//
//        return String.valueOf(maxGrade(secGrade, maintainGrade, relGrade));
//    }
//
//    private static char getSecurityGrade(int vulnerabilities) {
//        if (vulnerabilities == 0) return 'A';
//        else if (vulnerabilities == 1) return 'B';
//        else if (vulnerabilities == 2) return 'C';
//        else if (vulnerabilities <= 5) return 'D';
//        else return 'E';
//    }
//
//    private static char getMaintainabilityGrade(double techDebtRatio) {
//        if (techDebtRatio <= 5) return 'A';
//        else if (techDebtRatio <= 10) return 'B';
//        else if (techDebtRatio <= 20) return 'C';
//        else if (techDebtRatio <= 50) return 'D';
//        else return 'E';
//    }
//
//    private static char getReliabilityGrade(int bugs) {
//        if (bugs == 0) return 'A';
//        else if (bugs == 1) return 'B';
//        else if (bugs == 2) return 'C';
//        else if (bugs <= 5) return 'D';
//        else return 'E';
//    }
//
//    private static char maxGrade(char... grades) {
//        char worst = 'A';
//        for (char grade : grades) {
//            if (grade > worst) {
//                worst = grade;
//            }
//        }
//        return worst;
//    }
}
