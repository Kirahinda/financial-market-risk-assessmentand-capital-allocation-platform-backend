package com.ytfa.fmrcap.service;

import com.ytfa.fmrcap.entity.CapitalAllocation;
import com.ytfa.fmrcap.entity.RiskAssessment;
import com.ytfa.fmrcap.repository.CapitalAllocationRepository;
import com.ytfa.fmrcap.repository.RiskAssessmentRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CapitalAllocationService {

    private final CapitalAllocationRepository capitalAllocationRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;

    public CapitalAllocationService(
            CapitalAllocationRepository capitalAllocationRepository,
            RiskAssessmentRepository riskAssessmentRepository) {

        this.capitalAllocationRepository = capitalAllocationRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
    }

    @Transactional
    public CapitalAllocation save(CapitalAllocation allocation) {
        resolveRiskAssessment(allocation);
        validateAllocation(allocation, null);

        return capitalAllocationRepository.save(allocation);
    }

    public List<CapitalAllocation> findAll() {
        return capitalAllocationRepository.findAll();
    }

    public Optional<CapitalAllocation> findById(UUID id) {
        return capitalAllocationRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return capitalAllocationRepository.existsById(id);
    }

    @Transactional
    public CapitalAllocation update(UUID id, CapitalAllocation allocation) {

        if (!capitalAllocationRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Capital allocation not found: " + id);
        }

        allocation.setId(id);

        resolveRiskAssessment(allocation);
        validateAllocation(allocation, id);

        return capitalAllocationRepository.save(allocation);
    }

    @Transactional
    public void deleteById(UUID id) {

        if (!capitalAllocationRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Capital allocation not found: " + id);
        }

        capitalAllocationRepository.deleteById(id);
    }

    private void resolveRiskAssessment(CapitalAllocation allocation) {

        if (allocation == null
                || allocation.getRiskAssessment() == null
                || allocation.getRiskAssessment().getId() == null) {

            throw new IllegalArgumentException(
                    "A valid risk assessment ID is required");
        }

        UUID assessmentId = allocation.getRiskAssessment().getId();

        RiskAssessment assessment = riskAssessmentRepository
                .findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Risk assessment not found: " + assessmentId));

        allocation.setRiskAssessment(assessment);
    }

    private void validateAllocation(
            CapitalAllocation allocation,
            UUID allocationId) {

        BigDecimal percentage = allocation.getAllocatedPercent();

        if (percentage == null
                || percentage.compareTo(new BigDecimal("0.01")) < 0
                || percentage.compareTo(new BigDecimal("100.00")) > 0) {

            throw new IllegalArgumentException(
                    "Allocation percentage must be between 0.01 and 100.00");
        }

        UUID assessmentId = allocation.getRiskAssessment().getId();

        BigDecimal existingTotal =
                capitalAllocationRepository.sumAllocatedPercent(
                        assessmentId, allocationId);

        BigDecimal total = (existingTotal == null
                ? BigDecimal.ZERO
                : existingTotal).add(percentage);

        if (total.compareTo(new BigDecimal("100.00")) > 0) {

            throw new IllegalArgumentException(
                    "Total capital allocation cannot exceed 100%. "
                            + "Current total after this operation would be "
                            + total + "%");
        }
    }
}
