package com.ytfa.fmrcap.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "risk_assessment")
public class RiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Assessment date is required")
    private LocalDate assessmentDate;

    @NotNull(message = "Assessment status is required")
    @Enumerated(EnumType.STRING)
    private AssessmentStatus status;

    @NotBlank(message = "Portfolio name is required")
    private String portfolioName;

    @NotNull(message = "Capital USD is required")
    @Positive(message = "Capital USD must be greater than zero")
    @Column(precision = 19, scale = 2)
    private BigDecimal capitalUsd;

    @NotNull(message = "Investor is required")
    @ManyToOne
    @JoinColumn(name = "investor_id")
    private Investor investor;

    @NotNull(message = "Risk analyst is required")
    @ManyToOne
    @JoinColumn(name = "risk_analyst_id")
    private RiskAnalyst riskAnalyst;

    public RiskAssessment() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDate assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public AssessmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssessmentStatus status) {
        this.status = status;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public BigDecimal getCapitalUsd() {
        return capitalUsd;
    }

    public void setCapitalUsd(BigDecimal capitalUsd) {
        this.capitalUsd = capitalUsd;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public RiskAnalyst getRiskAnalyst() {
        return riskAnalyst;
    }

    public void setRiskAnalyst(RiskAnalyst riskAnalyst) {
        this.riskAnalyst = riskAnalyst;
    }
}
