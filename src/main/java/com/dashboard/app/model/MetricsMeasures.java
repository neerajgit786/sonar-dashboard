package com.dashboard.app.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class MetricsMeasures {

    private String metric;
    private String value;

}
