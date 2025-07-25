package com.dashboard.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_history")
@Data
public class History {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    private Master master;

    private String type;
    @Column(name = "gate_status")
    private String gateStatus;
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
