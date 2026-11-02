package com.ytfa.fmrcap.service;

import com.ytfa.fmrcap.entity.Investor;
import com.ytfa.fmrcap.entity.RiskAnalyst;
import com.ytfa.fmrcap.entity.RiskAssessment;

import com.ytfa.fmrcap.exception.ResourceConflictException;

import com.ytfa.fmrcap.repository.InvestorRepository;
import com.ytfa.fmrcap.repository.RiskAnalystRepository;
import com.ytfa.fmrcap.repository.RiskAssessmentRepository;
import com.ytfa.fmrcap.repository.CapitalAllocationRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RiskAssessmentService {

    private final RiskAssessmentRepository riskAssessmentRepository;
    private final InvestorRepository investorRepository;
    private final RiskAnalystRepository riskAnalystRepository;
    private final CapitalAllocationRepository capitalAllocationRepository;

    public RiskAssessmentService(
            RiskAssessmentRepository riskAssessmentRepository,
            InvestorRepository investorRepository,
            RiskAnalystRepository riskAnalystRepository,
            CapitalAllocationRepository capitalAllocationRepository) {

        this.riskAssessmentRepository = riskAssessmentRepository;
        this.investorRepository = investorRepository;
        this.riskAnalystRepository = riskAnalystRepository;
        this.capitalAllocationRepository = capitalAllocationRepository;
    }

    @Transactional
    public RiskAssessment save(RiskAssessment assessment) {

        validateAssessment(assessment);
        resolveRelationships(assessment);

        return riskAssessmentRepository.save(assessment);
    }

    public List<RiskAssessment> findAll() {
        return riskAssessmentRepository.findAll();
    }

    public Optional<RiskAssessment> findById(UUID id) {
        return riskAssessmentRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return riskAssessmentRepository.existsById(id);
    }

    @Transactional
    public RiskAssessment update(UUID id, RiskAssessment assessment) {

        if (!riskAssessmentRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Risk assessment not found: " + id);
        }

        assessment.setId(id);

        validateAssessment(assessment);
        resolveRelationships(assessment);

        return riskAssessmentRepository.save(assessment);
    }

    @Transactional
    public void deleteById(UUID id) {

        if (!riskAssessmentRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Risk assessment not found: " + id);
        }

        if (capitalAllocationRepository
                .existsByRiskAssessment_Id(id)) {

            throw new ResourceConflictException(
                    "Cannot delete this risk assessment because "
                    + "it has existing capital allocations. "
                    + "Remove or reassign those allocations first.");
        }

        riskAssessmentRepository.deleteById(id);
    }

    private void validateAssessment(RiskAssessment assessment) {

        if (assessment == null) {
            throw new IllegalArgumentException(
                    "Risk assessment information is required");
        }

        if (assessment.getPortfolioName() == null
                || assessment.getPortfolioName().isBlank()) {
            throw new IllegalArgumentException(
                    "Portfolio name is required");
        }

        if (assessment.getAssessmentDate() == null) {
            throw new IllegalArgumentException(
                    "Assessment date is required");
        }

        BigDecimal capitalUsd = assessment.getCapitalUsd();

        if (capitalUsd == null
                || capitalUsd.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Assessment capital must be greater than zero");
        }

        if (assessment.getStatus() == null) {
            throw new IllegalArgumentException(
                    "Assessment status is required");
        }
    }

    private void resolveRelationships(RiskAssessment assessment) {

        if (assessment.getInvestor() == null
                || assessment.getInvestor().getId() == null) {
            throw new IllegalArgumentException(
                    "A valid investor ID is required");
        }

        UUID investorId = assessment.getInvestor().getId();

        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Investor not found: " + investorId));

        assessment.setInvestor(investor);

        if (assessment.getRiskAnalyst() == null
                || assessment.getRiskAnalyst().getId() == null) {
            throw new IllegalArgumentException(
                    "A valid risk analyst ID is required");
        }

        UUID analystId = assessment.getRiskAnalyst().getId();

        RiskAnalyst riskAnalyst = riskAnalystRepository.findById(analystId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Risk analyst not found: " + analystId));

        assessment.setRiskAnalyst(riskAnalyst);
    }
}
