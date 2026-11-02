package com.ytfa.fmrcap.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "capital_allocation")
public class CapitalAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Instrument name is required")
    private String instrumentName;

    private String rationale;

    @NotNull(message = "Allocation percentage is required")
    @DecimalMin(value = "0.01",
            message = "Allocation must be at least 0.01%")
    @DecimalMax(value = "100.00",
            message = "Allocation cannot exceed 100%")
    @Column(precision = 5, scale = 2)
    private BigDecimal allocatedPercent;

    @NotNull(message = "Allocation date is required")
    private LocalDate allocationDate;

    @NotNull(message = "Risk assessment is required")
    @ManyToOne
    @JoinColumn(name = "risk_assessment_id")
    private RiskAssessment riskAssessment;

    public CapitalAllocation() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public BigDecimal getAllocatedPercent() {
        return allocatedPercent;
    }

    public void setAllocatedPercent(BigDecimal allocatedPercent) {
        this.allocatedPercent = allocatedPercent;
    }

    public LocalDate getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(LocalDate allocationDate) {
        this.allocationDate = allocationDate;
    }

    public RiskAssessment getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(RiskAssessment riskAssessment) {
        this.riskAssessment = riskAssessment;
    }
}
