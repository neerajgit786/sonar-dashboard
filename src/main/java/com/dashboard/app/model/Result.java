package com.dashboard.app.model;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class Result {
    public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getRag() {
		return rag;
	}

	public void setRag(String rag) {
		this.rag = rag;
	}

	public String grade;
    public String rag;

    public Result(String grade, String rag) {
        this.grade = grade;
        this.rag = rag;
    }
}
