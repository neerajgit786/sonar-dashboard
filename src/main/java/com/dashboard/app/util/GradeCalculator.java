package com.dashboard.app.util;

public class GradeCalculator {

    public static String calculateGrade(int vulnerabilities, double techDebtRatio, int bugs) {
        char secGrade = getSecurityGrade(vulnerabilities);
        char maintainGrade = getMaintainabilityGrade(techDebtRatio);
        char relGrade = getReliabilityGrade(bugs);

        return String.valueOf(maxGrade(secGrade, maintainGrade, relGrade));
    }

    private static char getSecurityGrade(int vulnerabilities) {
        if (vulnerabilities == 0) return 'A';
        else if (vulnerabilities == 1) return 'B';
        else if (vulnerabilities == 2) return 'C';
        else if (vulnerabilities <= 5) return 'D';
        else return 'E';
    }

    private static char getMaintainabilityGrade(double techDebtRatio) {
        if (techDebtRatio <= 5) return 'A';
        else if (techDebtRatio <= 10) return 'B';
        else if (techDebtRatio <= 20) return 'C';
        else if (techDebtRatio <= 50) return 'D';
        else return 'E';
    }

    private static char getReliabilityGrade(int bugs) {
        if (bugs == 0) return 'A';
        else if (bugs == 1) return 'B';
        else if (bugs == 2) return 'C';
        else if (bugs <= 5) return 'D';
        else return 'E';
    }

    private static char maxGrade(char... grades) {
        char worst = 'A';
        for (char grade : grades) {
            if (grade > worst) {
                worst = grade;
            }
        }
        return worst;
    }
}
