package com.ytfa.fmrcap.service;

import com.ytfa.fmrcap.entity.AssetClass;
import com.ytfa.fmrcap.entity.RiskAnalyst;
import com.ytfa.fmrcap.entity.TradingDesk;

import com.ytfa.fmrcap.exception.ResourceConflictException;

import com.ytfa.fmrcap.repository.AssetClassRepository;
import com.ytfa.fmrcap.repository.RiskAnalystRepository;
import com.ytfa.fmrcap.repository.RiskAssessmentRepository;
import com.ytfa.fmrcap.repository.TradingDeskRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RiskAnalystService {

    private final RiskAnalystRepository riskAnalystRepository;
    private final TradingDeskRepository tradingDeskRepository;
    private final AssetClassRepository assetClassRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;

    public RiskAnalystService(
            RiskAnalystRepository riskAnalystRepository,
            TradingDeskRepository tradingDeskRepository,
            AssetClassRepository assetClassRepository,
            RiskAssessmentRepository riskAssessmentRepository) {

        this.riskAnalystRepository = riskAnalystRepository;
        this.tradingDeskRepository = tradingDeskRepository;
        this.assetClassRepository = assetClassRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
    }

    @Transactional
    public RiskAnalyst save(RiskAnalyst riskAnalyst) {

        resolveRelationships(riskAnalyst);

        return riskAnalystRepository.save(riskAnalyst);
    }

    public List<RiskAnalyst> findAll() {
        return riskAnalystRepository.findAll();
    }

    public Optional<RiskAnalyst> findById(UUID id) {
        return riskAnalystRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return riskAnalystRepository.existsById(id);
    }

    @Transactional
    public RiskAnalyst update(UUID id, RiskAnalyst riskAnalyst) {

        if (!riskAnalystRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Risk analyst not found: " + id);
        }

        riskAnalyst.setId(id);

        resolveRelationships(riskAnalyst);

        return riskAnalystRepository.save(riskAnalyst);
    }

    @Transactional
    public void deleteById(UUID id) {

        if (!riskAnalystRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Risk analyst not found: " + id);
        }

        if (riskAssessmentRepository.existsByRiskAnalyst_Id(id)) {

            throw new ResourceConflictException(
                    "Cannot delete this risk analyst because "
                    + "existing risk assessments are assigned to this analyst. "
                    + "Reassign or remove those assessments first.");
        }

        riskAnalystRepository.deleteById(id);
    }

    private void resolveRelationships(RiskAnalyst riskAnalyst) {

        // Resolve the assigned trading desk.
        if (riskAnalyst.getTradingDesk() != null) {

            UUID deskId = riskAnalyst.getTradingDesk().getId();

            if (deskId == null) {
                throw new IllegalArgumentException(
                        "A valid trading desk ID is required");
            }

            TradingDesk tradingDesk = tradingDeskRepository.findById(deskId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Trading desk not found: " + deskId));

            riskAnalyst.setTradingDesk(tradingDesk);
        }

        // Resolve the assigned asset classes.
        List<AssetClass> managedAssetClasses = new ArrayList<>();

        if (riskAnalyst.getAssetClasses() != null) {

            for (AssetClass assetClass : riskAnalyst.getAssetClasses()) {

                if (assetClass.getId() == null) {
                    throw new IllegalArgumentException(
                            "Every asset class must have a valid ID");
                }

                AssetClass managedAssetClass =
                        assetClassRepository.findById(assetClass.getId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Asset class not found: "
                                                + assetClass.getId()));

                managedAssetClasses.add(managedAssetClass);
            }
        }

        riskAnalyst.setAssetClasses(managedAssetClasses);
    }
}
