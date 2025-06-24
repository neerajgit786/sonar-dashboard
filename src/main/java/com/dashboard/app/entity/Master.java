package com.dashboard.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "master")
@Data
@Getter
@Setter
public class Master {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    @Column(unique = true)
    private String key;
    private String report_url;
    private LocalDateTime date;
    @Column(name = "gate_status")
    private String gateStatus;
    @Column(name = "grade")
    private String grade;
}
