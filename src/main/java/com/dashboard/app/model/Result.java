package com.dashboard.app.model;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class Result {
    public String grade;
    public String rag;

    public Result(String grade, String rag) {
        this.grade = grade;
        this.rag = rag;
    }
}
