package com.dashboard.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
@Data
@Getter
@Setter
public class Metrics {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    private Master master;

    private String type;
    private Integer bugs;
    private Integer codeSmells;
    private Double coverage;
    private Integer vulnerabilities;
    @Column(name = "security_hotspots")
    private Integer securityHotspots;
    @Column(name = "sqale_debt_ratio")
    private Double maintainability;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
