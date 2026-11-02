package com.ytfa.fmrcap.repository;

import com.ytfa.fmrcap.entity.CapitalAllocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface CapitalAllocationRepository
        extends JpaRepository<CapitalAllocation, UUID> {

    @Query("""
            SELECT SUM(c.allocatedPercent)
            FROM CapitalAllocation c
            WHERE c.riskAssessment.id = :assessmentId
            AND (:allocationId IS NULL OR c.id <> :allocationId)
            """)
    BigDecimal sumAllocatedPercent(
            @Param("assessmentId") UUID assessmentId,
            @Param("allocationId") UUID allocationId);

    boolean existsByRiskAssessment_Id(UUID assessmentId);
}
