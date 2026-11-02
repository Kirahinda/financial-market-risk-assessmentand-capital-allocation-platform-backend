package com.ytfa.fmrcap.repository;

import com.ytfa.fmrcap.entity.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RiskAssessmentRepository
        extends JpaRepository<RiskAssessment, UUID> {

    boolean existsByInvestor_Id(UUID investorId);

    boolean existsByRiskAnalyst_Id(UUID analystId);
}