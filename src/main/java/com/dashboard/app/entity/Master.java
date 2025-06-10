package com.dashboard.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
    private LocalDate date;

    @Column(name = "gate_status")
    private String gateStatus;
}
