
package com.dashboard.app.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Data
@ToString
public class ProjectComponent {

	@JsonSerialize(using = ToStringSerializer.class)
	private String key;

	@JsonSerialize(using = ToStringSerializer.class)
	private String name;

	@JsonSerialize(using = ToStringSerializer.class)
	private String qualifier;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private LocalDateTime lastAnalysisDate;

	@JsonProperty("measures")
	private List<MetricsMeasures> metricsMeasuresList;
}
